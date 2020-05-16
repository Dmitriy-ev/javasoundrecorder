
package javasoundrecorder;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;


public class Main {
    public static void main(String[] args) throws Exception {
      
        String ACCESS_TOKEN = 
      "XsB4kkbZbrAAAAAAAAAAFEJ15AV1_8_vN15q2M3HVUhDTprX7iRvtF6S08avpy";
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        
        JavaSoundRecorder recorder = new JavaSoundRecorder(client);
        recorder.recordAudio(60000);
    }
}
