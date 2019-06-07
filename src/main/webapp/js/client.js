var quill = null;

window.onload = function() {

  quill = new Quill('#editor', {
    theme: 'snow'
  });

}



function clickme() {

    let input = { text : $("#text").val(),
                  voice : $("#voice").val(),
                  language : $("#language").val()
                };

    if ($("#usecustomlang").is(":checked")) input.language = $("#customlang").val();

    console.log(input);

    let myurl = "/example"
    if ($("#usessml").is(":checked")) myurl = "/exampleSSML"

    let xhr = new XMLHttpRequest();
    xhr.open('POST', encodeURI(myurl), true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.responseType = 'blob';
    xhr.onload = function(evt) {

        console.log(xhr.response);
        let blobUrl = URL.createObjectURL(new Blob([xhr.response], { type: "audio/mpeg" }));
        let audio = document.getElementById('myAudioElement');
        audio.src = blobUrl;

          let playPromise = audio.play();

          if (playPromise !== undefined) {
            playPromise.then(_ => {
              // Automatic playback started!
              console.log("success");
            })
            .catch(error => {
              // Auto-play was prevented
              console.log("error");
            });
          }
       }
    xhr.send(JSON.stringify(input));

}

function clickme2() {
    let l = $("#language").val()
    if ($("#usecustomlang").is(":checked")) l = $("#customlang").val();

    if ($("#usessml").is(":checked")) {
            let url = '/exampleSSML.mp3'+'?text='+$("#text").val()+'&voice=' +
                $("#voice").val() + "&language="+l;
            window.location.href = encodeURI(url);

    }
    else {
        let url = '/example.mp3'+'?text='+$("#text").val()+'&voice=' +
            $("#voice").val() + "&language="+l;
        window.location.href = encodeURI(url);
    }
}


function clickme3() {
    $("#eqdisplay").html($("#equation").val());
    MathJax.Hub.Queue(["Typeset",MathJax.Hub]);
}

