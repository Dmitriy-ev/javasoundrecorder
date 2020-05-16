
package javasoundrecorder;

import com.dropbox.core.v2.DbxClientV2;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class JavaSoundRecorder {
 
    private TargetDataLine line;
    private DbxClientV2 client;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMdd_HHmmss");

    public JavaSoundRecorder(DbxClientV2 client) throws Exception
    {
        this.client = client;
        DataLine.Info info = new DataLine.Info(
            TargetDataLine.class, getAudioFormat()
        );
        line = (TargetDataLine) AudioSystem.getLine(info);
    }

    public void recordAudio(long milliseconds)
    {
        String filePath = dateFormat.format(new Date()) + ".wav";
        File file = new File(filePath);
        start(file);
        stop(milliseconds, file);
    }

    public void start(File file)
    {
        Thread thread = new Thread(() -> {
            try {
                line.open(getAudioFormat());
                line.start();
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    public void stop(long milliseconds, File file)
    {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(milliseconds);
                line.stop();
                line.close();
                recordAudio(milliseconds);
                client.files().uploadBuilder("/" + file.getName())
                    .uploadAndFinish(new FileInputStream(file));
                file.delete();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    private static AudioFormat getAudioFormat()
    {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(
            sampleRate, sampleSizeInBits, channels, signed, bigEndian
        );
        return format;
    }
}

 
