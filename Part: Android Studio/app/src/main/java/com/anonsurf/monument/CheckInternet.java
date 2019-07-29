package com.anonsurf.monument;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import com.anonsurf.monument.authentication.Signin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

class CheckInternet extends AsyncTask<String,Void,Integer> {
    private Context context;

    public CheckInternet(Context context){
        this.context = context;
    }

    //check if the state of network is activated or not
    public boolean isConnected() {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null) {
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if (info!=null) {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
            else
                return false;
        }
        return false;
    }

    @Override
    protected Integer doInBackground(String... params) {
        int result = 0;

        if (isConnected()) {
            //to check connexion internet with 8.8.8.8 (dns google)
            try {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, 1500);
                socket.close();
                result = 1;
            } catch (IOException e) {
                Log.d("Error: ",e.getMessage());
            }

        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        final int res = result;

        //delay to launch SIGNIN Activity
        new CountDownTimer(2500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (res == 1) {
                    context.startActivity(new Intent(context, Signin.class));
                    if(context.toString().contains("NoConnection")){
                        NoConnection.getActivity().finish();
                    }
                    else{
                        Launchpage.getActivity().finish();
                    }
                }
                else{
                    if(!context.toString().contains("NoConnection")){
                        context.startActivity(new Intent(context, NoConnection.class));
                        Launchpage.getActivity().finish();
                    }
                }
            }
        }.start();
    }
}