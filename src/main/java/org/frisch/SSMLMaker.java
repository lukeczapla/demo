package org.frisch;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import java.io.FileOutputStream;
import java.io.OutputStream;


public class SSMLMaker {
    private String text = "";
    private String nameMP3 = "";
    private String languageCode = "";
    private String gender = "";
    private VoiceSelectionParams voice;

    public SSMLMaker() {
        text = "Hello world";
        languageCode = "en-US";
        gender = "NEUTRAL";
        nameMP3 = "test.mp3";
    }

    public SSMLMaker(Example e) {
        this.text = e.getText();
        nameMP3 = "test.mp3";
        languageCode = e.getLanguage();
        if (languageCode == null) languageCode = "en-US";

        if (e.getVoice() == 0) {
            gender = "MALE";
        }
        if (e.getVoice() == 1) {
            gender = "FEMALE";
        }
        if (e.getVoice() == 2) {
            gender = "NEUTRAL";
        }
    }


    public byte[] getMP3() throws Exception {

        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setSsml(text).build();


            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            // ("neutral")
            if (gender.equals("MALE")) {
                voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode(languageCode)
                        .setSsmlGender(SsmlVoiceGender.MALE)
                        .build();
            } else if (gender.equals("FEMALE")) {
                voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode(languageCode)
                        .setSsmlGender(SsmlVoiceGender.FEMALE)
                        .build();
            } else {
                voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode(languageCode)
                        .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                        .build();
            }

            // Select the type of audio file you want returned
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice,
                    audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            return audioContents.toByteArray();
            // Write the response to the output file.
        }
    }


    public void saveMP3() {
        try (OutputStream out = new FileOutputStream(nameMP3)) {
            out.write(getMP3());
            System.out.println("Written file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNameMP3() {
        return nameMP3;
    }

    public void setNameMP3(String nameMP3) {
        this.nameMP3 = nameMP3;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}


