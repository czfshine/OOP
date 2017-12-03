package cn.czfshine.game.gomoku.ai;

/**
 * @author:czfshine
 * @date:2017/11/23 0:00
 */

/**
 * 神经网络模型
 */

import com.google.gson.*;
import com.sun.deploy.net.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class NNModel {

    /**
     * 下一步
     * @return
     */
    public Point nextStep(int [][]data){
        try {
            return post(toJson(data));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new Point(7,7);
    }


    private String  toJson(int[][] a){
        Gson g = new Gson();
        String res=g.toJson(a);
        return res;
    }

    private Point post(String data) throws IOException {
        String res=Request.Post("http://localhost:8080/login")
                .bodyForm(Form.form().add("data",  data).build())
                .execute().returnContent().asString();

        Scanner i =new Scanner(res);
        int r=i.nextInt();
        int c=i.nextInt();

        return new Point(r,c);
    }



    public static void main(String[] args){
        NNModel nn= new NNModel();
        int n=15;
        int[][] a =new  int[n][n];
        for(int i = 0;i<n;i++){
            for(int j=0;j<n;j++){
                a[i][j]=0;
            }
        }
        System.out.println();
        try {
            nn.post(nn.toJson(a));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
