var player;
var seekbar;
var onBSTPlayerReady = function() {
    seekbar = new bstplayer.SeekBar(10, '_progress', null);
    seekbar.addEventListener('onSeekChanged', function(evt){
        player.setPlayPosition(evt.seekPosition * player.getMediaDuration());
    });

    player = new bstplayer.Player('Auto', 'demo/bst-player-js/tools/applause.mp3', false, null, null, null);
    player.addEventListener('onPlayState', function(evt){
        switch(evt.playState) {
            case 'Paused':
                document.getElementById('playButton').disabled = false;
                document.getElementById('pauseButton').disabled = true;
                document.getElementById('stopButton').disabled = false;
                break;
            case 'Started':
                document.getElementById('playButton').disabled = true;
                document.getElementById('pauseButton').disabled = false;
                document.getElementById('stopButton').disabled = false;
                break;
            case 'Stopped':
            case 'Finished':
                document.getElementById('playButton').disabled = false;
                document.getElementById('pauseButton').disabled = true;
                document.getElementById('stopButton').disabled = true;
                break;
        }
    });
    player.addEventListener('onPlayerState', function(evt){
        if(evt.playerState == 'Ready') {
            document.getElementById('playButton').disabled = false;
            document.getElementById('pauseButton').disabled = true;
            document.getElementById('stopButton').disabled = true;

            playTimer = window.setInterval(function(){
                seekbar.setPlayingProgress(player.getPlayPosition() /
                    player.getMediaDuration());
                document.getElementById('timer').innerHTML =
                (player.getPlayPosition() / 1000) + ' / ' +
                (player.getMediaDuration() / 1000);
            }, 1000);
        }
    });
    player.addEventListener('onLoadingProgress', function(evt){
        seekbar.setLoadingProgress(evt.progress);
    });
    player.addEventListener('onError', function(evt){
        window.alert(evt.message);
    });
    player.inject('_pid');
}