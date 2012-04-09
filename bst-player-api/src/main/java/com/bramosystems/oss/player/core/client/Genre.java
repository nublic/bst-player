/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bramosystems.oss.player.core.client;

/**
 * A enumeration of genres as defined in <a href="http://www.id3.org/id3v2.3.0">ID3v1</a>
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.0
 */
public enum Genre {
    Blues("Blues"),
    Classic_Rock("Classic Rock"),
    Country("Country"),
    Dance("Dance"),
    Disco("Disco"),
    Funk("Funk"),
    Grunge("Grunge"),
    Hip_Hop("Hip-Hop"),
    Jazz("Jazz"),
    Metal("Metal"),
    New_Age("New Age"),
    Oldies("Oldies"),
    Other("Other"),
    Pop("Pop"),
    RnB("R&B"),
    Rap("Rap"),
    Reggae("Reggae"),
    Rock("Rock"),
    Techno("Techno"),
    Industrial("Industrial"),
    Alternative("Alternative"),
    Ska("Ska"),
    Death_Metal("Death Metal"),
    Pranks("Pranks"),
    Soundtrack("Soundtrack"),
    Euro_Techno("Euro-Techno"),
    Ambient("Ambient"),
    Trip_Hop("Trip-Hop"),
    Vocal("Vocal"),
    Jazz_Funk("Jazz+Funk"),
    Fusion("Fusion"),
    Trance("Trance"),
    Classical("Classical"),
    Instrumental("Instrumental"),
    Acid("Acid"),
    House("House"),
    Game("Game"),
    Sound_Clip("Sound Clip"),
    Gospel("Gospel"),
    Noise("Noise"),
    AlternRock("AlternRock"),
    Bass("Bass"),
    Soul("Soul"),
    Punk("Punk"),
    Space("Space"),
    Meditative("Meditative"),
    Instrumental_Pop("Instrumental Pop"),
    Instrumental_Rock("Instrumental Rock"),
    Ethnic("Ethnic"),
    Gothic("Gothic"),
    Darkwave("Darkwave"),
    Techno_Industrial("Techno-Industrial"),
    Electronic("Electronic"),
    Pop_Folk("Pop-Folk"),
    Eurodance("Eurodance"),
    Dream("Dream"),
    Southern_Rock("Southern Rock"),
    Comedy("Comedy"),
    Cult("Cult"),
    Gangsta("Gangsta"),
    Top_40("Top 40"),
    Christian_Rap("Christian Rap"),
    Pop_Funk("Pop/Funk"),
    Jungle("Jungle"),
    Native_American("Native American"),
    Cabaret("Cabaret"),
    New_Wave("New Wave"),
    Psychadelic("Psychadelic"),
    Rave("Rave"),
    Showtunes("Showtunes"),
    Trailer("Trailer"),
    Lo_Fi("Lo-Fi"),
    Tribal("Tribal"),
    Acid_Punk("Acid Punk"),
    Acid_Jazz("Acid Jazz"),
    Polka("Polka"),
    Retro("Retro"),
    Musical("Musical"),
    Rock_n_Roll("Rock & Roll"),
    Hard_Rock("Hard Rock"),
    Folk("Folk"),
    Folk_Rock("Folk-Rock"),
    National_Folk("National Folk"),
    Swing("Swing"),
    Fast_Fusion("Fast Fusion"),
    Bebob("Bebob"),
    Latin("Latin"),
    Revival("Revival"),
    Celtic("Celtic"),
    Bluegrass("Bluegrass"),
    Avantgarde("Avantgarde"),
    Gothic_Rock("Gothic Rock"),
    Progressive_Rock("Progressive Rock"),
    Psychedelic_Rock("Psychedelic Rock"),
    Symphonic_Rock("Symphonic Rock"),
    Slow_Rock("Slow Rock"),
    Big_Band("Big Band"),
    Chorus("Chorus"),
    Easy_Listening("Easy Listening"),
    Acoustic("Acoustic"),
    Humour("Humour"),
    Speech("Speech"),
    Chanson("Chanson"),
    Opera("Opera"),
    Chamber_Music("Chamber Music"),
    Sonata("Sonata"),
    Symphony("Symphony"),
    Booty_Bass("Booty Bass"),
    Primus("Primus"),
    Porn_Groove("Porn Groove"),
    Satire("Satire"),
    Slow_Jam("Slow Jam"),
    Club("Club"),
    Tango("Tango"),
    Samba("Samba"),
    Folklore("Folklore"),
    Ballad("Ballad"),
    Power_Ballad("Power Ballad"),
    Rhythmic_Soul("Rhythmic Soul"),
    Freestyle("Freestyle"),
    Duet("Duet"),
    Punk_Rock("Punk Rock"),
    Drum_Solo("Drum Solo"),
    A_capella("A capella"),
    Euro_House("Euro-House"),
    Dance_Hall("Dance Hall");

    private String name;

    private Genre(String name) {
        this.name = name;
    }

    /**
     * Returns a "human friendly" name of the Genre
     *
     * @return a "human friendly" name of the Genre
     */
    @Override
    public String toString() {
        return name;
    }


}
