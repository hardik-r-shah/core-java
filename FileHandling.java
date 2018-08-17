import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

class FileLineReader implements Runnable {
    int startIndex;
    int numLinesToSkip;

    public FileLineReader(int startIndex,int numLinesToSkip){
        this.startIndex =startIndex;
        this.numLinesToSkip = numLinesToSkip;
    }

    @Override
    public void run() {
            File f = new File("C:\\tmp\\demo.txt");
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                int curLine=0;
                for(int i=0;i<startIndex;i++){
                    br.readLine();
                    curLine++;
                }
                int nextLine = curLine;
                while((line = br.readLine())!=null){
                    if(curLine==nextLine){
                        System.out.println(Thread.currentThread().getName()+"----"+line);
                        nextLine = curLine+numLinesToSkip+1;
                    }
                    curLine++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}

public class FileHandling {

    public static void main(String[] args) {

        int totalThreads = 5;


        ExecutorService exec = new ThreadPoolExecutor(totalThreads, totalThreads, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
        for(int i=0;i<totalThreads;i++){
            Runnable r =new FileLineReader(i,totalThreads-1);
            exec.submit(r);
        }

        exec.shutdown();
        try {
            exec.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
