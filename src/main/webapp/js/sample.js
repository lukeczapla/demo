/*
let record = document.querySelector('.record');
let stop = document.querySelector('.stop');
let soundClips = document.querySelector('.sound-clips');
let canvas = document.querySelector('.visualizer');
console.log(canvas);
let mainSection = document.querySelector('.main-controls');
var mediaRecorder = null;
stop.disabled = true;

let audioCtx = new (window.AudioContext || webkitAudioContext)();
let canvasCtx = canvas.getContext("2d");

  let chunks = [];


  var onSuccess = function(stream) {
     mediaRecorder = new MediaRecorder(stream);//, {mimeType: "audio/ogg", samplingRate:16000});
 //   mediaRecorder.mimeType = "audio/ogg";
 //   visualize(stream);

    record.onclick = function() {
      mediaRecorder.start();
      console.log(mediaRecorder.state + " " + mediaRecorder.mimeType + " " + mediaRecorder.audioBitsPerSecond);
      console.log("recorder started");
      record.style.background = "red";

      stop.disabled = false;
      record.disabled = true;
    }

    stop.onclick = function() {
      mediaRecorder.stop();
      console.log(mediaRecorder.state);
      console.log("recorder stopped");
      record.style.background = "";
      record.style.color = "";
      // mediaRecorder.requestData();

      stop.disabled = true;
      record.disabled = false;
    }

    mediaRecorder.onstop = function(e) {
      console.log("data available after MediaRecorder.stop() called.");

      var clipName = prompt('Enter a name for your sound clip?','My unnamed clip');
      console.log(clipName);
      var clipContainer = document.createElement('article');
      var clipLabel = document.createElement('p');
      var audio = document.createElement('audio');
      var deleteButton = document.createElement('button');

      clipContainer.classList.add('clip');
      audio.setAttribute('controls', '');
      deleteButton.textContent = 'Delete';
      deleteButton.className = 'delete';

      if(clipName === null) {
        clipLabel.textContent = 'My unnamed clip';
      } else {
        clipLabel.textContent = clipName;
      }

      clipContainer.appendChild(audio);
      clipContainer.appendChild(clipLabel);
      clipContainer.appendChild(deleteButton);
      soundClips.appendChild(clipContainer);

      audio.controls = true;
      var blob = new Blob(chunks, { 'type' : 'audio/webm;codecs=pcm' });
      chunks = [];
      var audioURL = window.URL.createObjectURL(blob);
      let reader = new FileReader();
      let base64data = null;
      reader.readAsDataURL(blob);
      reader.onloadend = function() {
            base64data = reader.result;
            console.log(base64data);
            // we'll send this...
            let req = {
                headers: {
                     'Content-Type': 'application/json'
                },
                url: "/translate",
                method: "POST",
                data: base64data
            };
            $.ajax(req).done(function(data) {
                console.log(data);
            });
      }
      audio.src = audioURL;
      console.log("recorder stopped");

      deleteButton.onclick = function(e) {
        evtTgt = e.target;
        evtTgt.parentNode.parentNode.removeChild(evtTgt.parentNode);
      }

      clipLabel.onclick = function() {
        var existingName = clipLabel.textContent;
        var newClipName = prompt('Enter a new name for your sound clip?');
        if(newClipName === null) {
          clipLabel.textContent = existingName;
        } else {
          clipLabel.textContent = newClipName;
        }
      }
    }

    mediaRecorder.ondataavailable = function(e) {
      chunks.push(e.data);
    }
  }

  var onError = function(err) {
    console.log('The following error occured: ' + err);
  }


  navigator.mediaDevices.getUserMedia({audio: true}).then(onSuccess, onError);


function visualize(stream) {
  let source = audioCtx.createMediaStreamSource(stream);

  let analyser = audioCtx.createAnalyser();
  analyser.fftSize = 2048;
  let bufferLength = analyser.frequencyBinCount;
  let dataArray = new Uint8Array(bufferLength);

  //source.connect(analyser);
  analyser.connect(audioCtx.destination);

  draw()

  function draw() {
    WIDTH = canvas.width
    HEIGHT = canvas.height;

    requestAnimationFrame(draw);

    analyser.getByteTimeDomainData(dataArray);

    canvasCtx.fillStyle = 'rgb(200, 200, 200)';
    canvasCtx.fillRect(0, 0, WIDTH, HEIGHT);

    canvasCtx.lineWidth = 2;
    canvasCtx.strokeStyle = 'rgb(0, 0, 0)';

    canvasCtx.beginPath();

    let sliceWidth = WIDTH * 1.0 / bufferLength;
    let x = 0;


    for(let i = 0; i < bufferLength; i++) {

      let v = dataArray[i] / 128.0;
      let y = v * HEIGHT/2;

      if (i === 0) {
        canvasCtx.moveTo(x, y);
      } else {
        canvasCtx.lineTo(x, y);
      }

      x += sliceWidth;
    }

    canvasCtx.lineTo(canvas.width, canvas.height/2);
    canvasCtx.stroke();

  }
}



window.onresize = function() {
  canvas.width = mainSection.offsetWidth;
}

window.onresize();
*/

$.support.cors = true;

var mediaRecorder = null;
var record = document.querySelector('.record'),
    stop = document.querySelector('.stop');

window.onload = function() {

    var mediaConstraints = {
        audio: true
    };

    navigator.getUserMedia(mediaConstraints, onMediaSuccess, onMediaError);

}

function onMediaSuccess(stream) {
    mediaRecorder = new MediaStreamRecorder(stream);
    mediaRecorder.sampleRate = 44100;
    mediaRecorder.bufferSize = 0;
    mediaRecorder.audioChannels = 1;
    mediaRecorder.recorderType = StereoAudioRecorder;
    mediaRecorder.mimeType = 'audio/wav'; // audio/webm or audio/ogg or audio/wav

        mediaRecorder.ondataavailable = function (blob) {
        // POST/PUT "Blob" using FormData/XHR2
              let reader = new FileReader();
              let base64data = null;
              console.log("Reading blob");
              reader.readAsDataURL(blob);
              reader.onloadend = function() {
                    base64data = reader.result;
                    console.log(base64data);
                    // we'll send this...
                    let req = {
                        headers: {
                             'Content-Type': 'application/json'
                        },
                        url: "/translate",
                        method: "POST",
                        data: base64data
                    };
                    $.ajax(req).done(function(data) {
                        console.log(data);
                        $("#textresult").text(data);
                    });
              }
        var blobURL = URL.createObjectURL(blob);
        var audio = document.getElementById("player");
        audio.src = blobURL;
        audio.play();

        //document.write('<a href="' + blobURL + '">' + blobURL + '</a>');
    };
//    mediaRecorder.start(15000);
}

function onMediaError(e) {
    console.error('media error', e);
}

record.onclick = function() {
  mediaRecorder.start(15000);
  console.log(mediaRecorder.state + " " + mediaRecorder.mimeType + " " + mediaRecorder.audioBitsPerSecond);
  console.log("recorder started");
  record.style.background = "red";

  stop.disabled = false;
  record.disabled = true;
}

stop.onclick = function() {
  mediaRecorder.stop();
  console.log(mediaRecorder.state);
  console.log("recorder stopped");
  record.style.background = "";
  record.style.color = "";
  // mediaRecorder.requestData();

  stop.disabled = true;
  record.disabled = false;
}
