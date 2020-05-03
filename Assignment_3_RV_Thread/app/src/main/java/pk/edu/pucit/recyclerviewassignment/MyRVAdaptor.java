package pk.edu.pucit.recyclerviewassignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyRVAdaptor  extends RecyclerView.Adapter<MyRVAdaptor.ViewHolder>{
    private static final String TAG = "RecyclerViewAdaptor";
    private  ArrayList<MyBookInfo> books;
    private boolean permissionsGranted;
    private  Context mContext;
    private Handler mHandler;
    private ProgressDialog pDialog;



    public MyRVAdaptor(Context mContext, ArrayList<MyBookInfo> books, boolean permissionsGranted) {
        this.books = books;
        this.mContext = mContext;
        this.permissionsGranted = permissionsGranted;
        mHandler = new Handler ();
        pDialog = new ProgressDialog (mContext);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder:  called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout,parent , false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        String path = "https://github.com/revolunet/PythonBooks/blob/master/"+books.get(position).getBookCover()+"?raw=true";
        String btnText = null;
        Glide.with(mContext)
                .asBitmap()
                .load(String.format("%s",path))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.bookImage);

        final String url = books.get (position).getBookUrl ();
        String[] urlParts = url.split ("\\.");
        String urlLastPart = urlParts[urlParts.length-1];
        final String fileName = books.get (position).getBookTitle ()+"."+urlLastPart;
        if(urlLastPart.equals ("pdf") || urlLastPart.equals ("zip")){
            btnText = "DOWNLOAD";
        }
        else{
            btnText = "READ ONLINE";
        }

        holder.bookTitle.setText(books.get(position).getBookTitle());
        holder.bookLevel.setText(books.get(position).getBookLevel());
        holder.bookDes.setText(books.get(position).getBookInfo());
        holder.bookBtn.setText(String.format("%s",btnText));
        if(btnText.equals ("DOWNLOAD")){
            holder.bookBtn.setOnClickListener (new View.OnClickListener ()
            {
                @Override
                public void onClick(View v)
                {
                    pDialog.setMessage("Downloading file. Please wait...");
                    pDialog.setIndeterminate(false);
                    pDialog.setMax(100);
                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setCancelable(true);
                    pDialog.show();
                    Runnable runnabale = new Runnable ()
                    {
                        @Override
                        public void run()
                        {
                            if(downloadBook (url, fileName)){
                                mHandler.post (
                                          new Runnable ()
                                          {
                                              @Override
                                              public void run()
                                              {
                                                  Toast.makeText (mContext,
                                                            "finished downloading",
                                                            Toast.LENGTH_SHORT).show ();
                                              }
                                          });
                            }
                        }
                    };
                   Thread th = new Thread (runnabale);
                    th.start ();
                }
            });
        }
        else{
            holder.bookBtn.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    final Intent intent1 = intent.setData (Uri.parse (url));
                    mContext.startActivity (intent);
                }
            });
        }

    }

    private boolean downloadBook(String fileUrl, String filename) {
        boolean flag = false;

        if(this.permissionsGranted){
            try {
                File folder = new File(Environment.getExternalStorageDirectory ()+"/downloads");
                folder.mkdir ();
                URL url = new URL(fileUrl);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.setDoOutput(true);
                httpConn.connect();
                final int lenghtOfFile = httpConn.getContentLength();




                File file = new File (folder,filename);
                file.createNewFile ();
                FileOutputStream fos = new FileOutputStream(file);

                InputStream is = httpConn.getInputStream();

                byte[] buffer = new byte[1024*1024];
                long total = 0;
                int bytesToWrite = 0;

                while ((bytesToWrite = is.read(buffer)) != -1) {

                    total += bytesToWrite;
                    final long finalTotal = total;
                    mHandler.post (new Runnable () {
                                    @Override
                                    public void run() {
                                        int percentage = (int) ((finalTotal *100)/lenghtOfFile);
                                        pDialog.setProgress (percentage);
                                    }});
                    fos.write(buffer, 0, bytesToWrite);
                }
                is.close();
                fos.flush ();
                fos.close();
                flag = true;
            }
            catch (Exception e) {

                flag = false;
                Toast.makeText (mContext, e.getMessage (), Toast.LENGTH_LONG).show ();
            }

        }
        else{

        }
        pDialog.dismiss ();
        return flag;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView bookImage;
        private TextView bookTitle;
        private TextView bookLevel;
        private TextView bookDes;
        private Button bookBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.BookImageId1);
            bookTitle = itemView.findViewById(R.id.BookTitleId);
            bookLevel = itemView.findViewById(R.id.BookLevelId);
            bookDes = itemView.findViewById(R.id.BookDesId);
            bookBtn = itemView.findViewById(R.id.BookBtnId);
            ConstraintLayout parentLayout = itemView.findViewById (R.id.rv_item);
        }
    }

}
