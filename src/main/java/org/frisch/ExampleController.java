package org.frisch;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class ExampleController {

    @PostMapping(value = "/exampleSSML", produces = "audio/mpeg")
    @JsonView({JsonExample.class})
    public byte[] generateInfoSSML(@RequestBody Example input) throws Exception {

        SSMLMaker myttsMaker = new SSMLMaker(input);
        return myttsMaker.getMP3();

    }

    @PostMapping(value = "/example", produces = "audio/mpeg")
    @JsonView({JsonExample.class})
    public byte[] generateInfo(@RequestBody Example input) throws Exception {

        TTSMaker myttsMaker = new TTSMaker(input);
        return myttsMaker.getMP3();

    }

    /**
     * This was for testing an Arduino interface to NodeMCU
     * @param whatever anything in the request
     * @return anything you want
     */
    @PostMapping(value = "/chitchat")
    public String generateJoke(@RequestBody String whatever) {
        System.out.println(whatever);
        return "hello";
    }]\
    @PostMapping(value = "/translate")
    public String generateText(@RequestBody String base64Audio) {
        Base64.Decoder decoder = Base64.getDecoder();
        //System.out.println(base64Audio);
        byte[] recordedBytes = decoder.decode(base64Audio.split(",")[1]);
        TextMaker textMaker = new TextMaker(recordedBytes);
        String result = textMaker.getText();
        System.out.println(result);
        return result;
    }

    @GetMapping(value = "/example.mp3", produces = "audio/mpeg")
    @JsonView({JsonExample.class})
    public byte[] generateInfoGet(@RequestParam(value="text", required=true) String text, @RequestParam(value="voice", required=false) Integer voice,
                                  @RequestParam(value="language", required=false) String language) throws Exception {

        Example input = new Example();
        input.setText(text);
        if (voice == null) {
            input.setVoice(0);
        } else input.setVoice(voice);

        if (language == null) {
            input.setLanguage("en-US");
        } else input.setLanguage(language);
        TTSMaker myttsMaker = new TTSMaker(input);
        return myttsMaker.getMP3();

    }

    @GetMapping(value = "/exampleSSML.mp3", produces = "audio/mpeg")
    @JsonView({JsonExample.class})
    public byte[] generateInfo2Get(@RequestParam(value="text", required=true) String text, @RequestParam(value="voice", required=false) Integer voice,
                                  @RequestParam(value="language", required=false) String language) throws Exception {

        Example input = new Example();
        input.setText(text);
        if (voice == null) {
            input.setVoice(0);
        } else input.setVoice(voice);

        if (language == null) {
            input.setLanguage("en-US");
        } else input.setLanguage(language);
        SSMLMaker myttsMaker = new SSMLMaker(input);
        return myttsMaker.getMP3();

    }

}
