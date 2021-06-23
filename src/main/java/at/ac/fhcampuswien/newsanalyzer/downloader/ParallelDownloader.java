package at.ac.fhcampuswien.newsanalyzer.downloader;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Erstellt von Alexander Ressl am 23.06.2021
 */
public class ParallelDownloader extends Downloader{

    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public int process(List<String> urls) {
        long start = System.nanoTime();
        int count = 0;
        for (String url : urls) {
            try {
                Future<?> taskStatus = executorService.submit(() -> {
                    System.out.println("Saving url " + url);
                    saveUrl2File(url);
                });
            } catch (Exception e) {
                System.err.println("Error in thread: " + Thread.currentThread().getName());
            }
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            System.err.println("Error while shutting down!" + ex);
            executorService.shutdownNow();
        }
        long end = System.nanoTime();
        System.out.println("Time: "+ (end - start)/1000000 + " milliseconds");
        return count;
    }
}
