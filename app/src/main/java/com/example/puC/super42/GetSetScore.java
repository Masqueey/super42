package com.example.puC.super42;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Mark on 5-6-2016.
 */
public class GetSetScore {
    Context c;

    public GetSetScore(Context c) {
        this.c = c;
    }
    /**
     * Writes a highscore
     *
     * @param score : the highscore to save
     */
    public void saveScore(String score) {
        Log.d("saveScore", "started");
        File dir  = new File(c.getApplicationInfo().dataDir);
        dir.mkdir();
        File myFile = new File(dir, "highscores.txt");
        try {
            Log.d("saveScore", "s=" + score + " trying.. sd=" + dir.toString());
            if (!myFile.exists()) {
                try {
                    myFile.createNewFile();
                } catch (Exception e) {
                    Log.d("saveScore", "myFile.createNewFile() failed " + e.toString());
                }
            }
            FileOutputStream out = new FileOutputStream(myFile, true);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            String append = score + " " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            if (Pattern.matches("\\A\\d+\\s\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\Z", append)) {
                writer.append("\n" + append);
            }
            writer.close();
            out.close();
            Log.d("saveScore", "succes");
        } catch (Exception e) {
            Log.d("saveScore", "Exception " + e.toString());
        }
        readHighscores();
    }


    /**
     * Reads all the highscores and sorts them
     * @return : a sorted list of scores witht the dates
     */
    public ArrayList<String> readHighscores() {
        File dir  = new File(c.getApplicationInfo().dataDir);
        File file = new File(dir, "highscores.txt");
        ArrayList<String> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s = br.readLine()) != null) {
                if (Pattern.matches("\\A\\d+\\s\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\Z", s)) {
                    res.add(s);
                }
            }
            br.close();
        } catch (IOException e) {
            Log.d("readHighscores", e.toString());
        }
        Log.d("readHighscores", "res=" + res + " sorted=" + bublesort(res));
        return res;
    }


    /**
     * A bubble sort implementation for the scores
     * @param list : the list to sort
     * @return : the sorted list
     */
    private static ArrayList<String> bublesort (ArrayList<String> list) {
        boolean swap = true;
        Pattern p = Pattern.compile("\\A\\d+");

        while(swap){

            swap = false;

            for(int i=0; i<list.size()-1; i++){
                String s0 = list.get(i);
                String s1 = list.get(i+1);
                Matcher m0 = p.matcher(s0);
                Matcher m1 = p.matcher(s1);
                if (!m0.find() || !m1.find()) {
                    throw new IllegalArgumentException("Illegal list items");
                }
                Log.d("bublesort", "s0=" + s0 + " s1=" + s1);
                if(Integer.parseInt(m0.group(0)) > Integer.parseInt(m1.group(0))){
                    list.set(i, s1);
                    list.set(i+1, s0);
                    swap = true;
                }
            }
        }
        return list;
    }
}
