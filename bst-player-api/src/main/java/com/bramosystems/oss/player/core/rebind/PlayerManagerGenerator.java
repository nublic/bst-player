/*
 *  Copyright 2010 Sikiru.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.bramosystems.oss.player.core.rebind;

import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sikiru
 */
public class PlayerManagerGenerator extends Generator {

    private HashMap<String, String> provClassMap = new HashMap<String, String>();
    private HashMap<String, HashSet<String>> playerMap2 = new HashMap<String, HashSet<String>>();
    private String className = null, packageName = null;
    private TreeLogger logger;

    public PlayerManagerGenerator() {
        // init plugin props ...
    }

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName)
            throws UnableToCompleteException {
        this.logger = logger;
        TypeOracle typeOracle = context.getTypeOracle();

        try {
            // get classType and save instance variables
            JClassType classType = typeOracle.getType(typeName);
            packageName = classType.getPackage().getName();
            className = classType.getSimpleSourceName() + "Impl";

            // Generate class source code
            generateClass(logger, context);
        } catch (Exception e) {
            logger.log(TreeLogger.ERROR, "Unable to build Player Widgets!", e);
        }

        // return the fully qualifed name of the class generated
        return packageName + "." + className;
    }

    private void collatePlayers(TypeOracle typeOracle) {
        logger.log(TreeLogger.Type.INFO, "Searching for Player Providers");
        JClassType a[] = typeOracle.getTypes();
        for (int i = 0; i < a.length; i++) {
            if (a[i].isAnnotationPresent(PlayerProvider.class)) {
                String pName = a[i].getAnnotation(PlayerProvider.class).value();
                provClassMap.put(a[i].getQualifiedSourceName(), pName);
                playerMap2.put(pName, new HashSet<String>());
            }
        }
        logger.log(TreeLogger.Type.INFO, "Searching for Player widgets");
        for (int i = 0; i < a.length; i++) {
            if (a[i].isAnnotationPresent(Player.class)) {
                String name = a[i].getAnnotation(Player.class).providerFactory().getName();
                if (provClassMap.containsKey(name)) {
                    logger.log(TreeLogger.Type.INFO, "Processing Player widget : " + a[i].getQualifiedSourceName());
                    playerMap2.get(provClassMap.get(name)).add(a[i].getQualifiedSourceName());
                } else {
                    logger.log(TreeLogger.Type.ERROR, "WidgetFactory '" + name + "' should be annotated with @PlayerProvider");
                }
            }
        }
    }

    /**
     * @param logger Logger object
     * @param context Generator context
     */
    private void generateClass(TreeLogger logger, GeneratorContext context) throws NotFoundException {
        // get print writer that receives the source code
        PrintWriter printWriter = context.tryCreate(logger, packageName, className);

        // print writer if null, source code has ALREADY been generated,  return
        if (printWriter == null) {
            return;
        }

        collatePlayers(context.getTypeOracle());

        // init composer, set class properties, create source writer
        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);
        composer.setSuperclass("PlayerManager");
        composer.addImport("com.google.gwt.core.client.GWT");
        composer.addImport("com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory");
        composer.addImport("com.bramosystems.oss.player.core.client.*");
        composer.addImport("java.util.*");

        SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

        sourceWriter.println("private HashMap<String, HashMap<String, PlayerInfo>> pInfos = new HashMap<String, HashMap<String, PlayerInfo>>();");

        // collate widget factories & create static holders ...
        Iterator<String> fact = provClassMap.keySet().iterator();
        while (fact.hasNext()) {
            String provClass = fact.next();
            String provName = provClassMap.get(provClass);
            sourceWriter.println("private static PlayerProviderFactory pwf_" + provName + " = GWT.create(" + provClass + ".class);");
        }
        sourceWriter.println();

        Pattern ptrn = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");

        // generate constructor source code
        sourceWriter.println("public " + className + "() { ");
        sourceWriter.indent();
        fact = playerMap2.keySet().iterator();
        while (fact.hasNext()) {
            String provName = fact.next();
            sourceWriter.println("pInfos.put(\"" + provName + "\", new HashMap<String, PlayerInfo>());");

            Iterator<String> pns = playerMap2.get(provName).iterator();
            while (pns.hasNext()) {
                JClassType jt = context.getTypeOracle().getType(pns.next());
                Player p = jt.getAnnotation(Player.class);
                boolean ps = false, ms = false;

                JClassType ints[] = jt.getImplementedInterfaces();
                for (int j = 0; j < ints.length; j++) {
                    if (ints[j].getQualifiedSourceName().equals(MatrixSupport.class.getName())) {
                        ms = true;
                    } else if (ints[j].getQualifiedSourceName().equals(PlaylistSupport.class.getName())) {
                        ps = true;
                    }
                }
                Matcher m = ptrn.matcher(p.minPluginVersion());
                if (m.matches()) {
                    sourceWriter.println("pInfos.get(\"" + provName + "\").put(\"" + p.name()
                            + "\", new PlayerInfo(\"" + provName + "\",\"" + p.name() + "\"," + "PluginVersion.get("
                            + Integer.parseInt(m.group(1)) + "," + Integer.parseInt(m.group(2)) + "," + Integer.parseInt(m.group(3)) + "),"
                            + ps + "," + ms + "));");
                } else {
                    logger.log(TreeLogger.Type.WARN, "Min");
                }
            }
        }
        sourceWriter.outdent();
        sourceWriter.println("}");   // end constructor source generation

        sourceWriter.println();

        // implement get player names ...
        sourceWriter.println("@Override");
        sourceWriter.println("public Set<String> getPlayerNames(String providerName) {");
        sourceWriter.indent();
        sourceWriter.println("if(!pInfos.containsKey(providerName))");
        sourceWriter.println("throw new IllegalArgumentException(\"Unknown player provider - \" + providerName);");
        sourceWriter.println("return pInfos.get(providerName).keySet();");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // implement get provider names ...
        sourceWriter.println("@Override");
        sourceWriter.println("public Set<String> getProviders(){");
        sourceWriter.indent();
        sourceWriter.println("return pInfos.keySet();");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // implement get player names ...
        sourceWriter.println("@Override");
        sourceWriter.println("public PlayerInfo getPlayerInfo(String providerName, String playerName) {");
        sourceWriter.indent();
        sourceWriter.println("if(!pInfos.containsKey(providerName))");
        sourceWriter.println("throw new IllegalArgumentException(\"Unknown player provider - \" + providerName);");
        sourceWriter.println("if(!pInfos.get(providerName).containsKey(playerName))");
        sourceWriter.println("throw new IllegalArgumentException(\"Unknown player name - \" + playerName);");
        sourceWriter.println("return pInfos.get(providerName).get(playerName);");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // implement get widget factory with defered binding on demand ....
        sourceWriter.println("@Override");
        sourceWriter.println("public PlayerProviderFactory getProviderFactory(String provider) {");
        sourceWriter.indent();
        sourceWriter.println("PlayerProviderFactory wf = null;");

        boolean firstRun = true;
        fact = provClassMap.values().iterator();
        while (fact.hasNext()) {
            String provName = fact.next();
            if (firstRun) {
                sourceWriter.println("if(\"" + provName + "\".equals(provider)) {");
            } else {
                sourceWriter.println("else if(\"" + provName + "\".equals(provider)) {");
            }
            sourceWriter.indent();
            sourceWriter.println("wf = pwf_" + provName + ";");
            sourceWriter.outdent();
            sourceWriter.println("}");
            firstRun = false;
        }
        sourceWriter.println("return wf;");
        sourceWriter.outdent();
        sourceWriter.println("}");
        sourceWriter.println();

        // close generated class
        sourceWriter.outdent();
        sourceWriter.println("}");

        // commit generated class
        context.commit(logger, printWriter);
    }
}
