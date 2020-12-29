package com.example.sgh17122019;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReportActivity extends AppCompatActivity {

    public class PDFDoc {
        int id;
        String name,category,pdfURL,pdfIconURL;
        public int getId() {return id;}
        public void setId(int id) {this.id = id;}
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        public String getAuthor() {return category;}
        public void setCategory(String category) {this.category = category;}
        public String getPdfURL() {return pdfURL;}
        public void setPdfURL(String pdfURL) {this.pdfURL = pdfURL;}
        public String getPdfIconURL() {return pdfIconURL;}
        public void setPdfIconURL(String pdfIconURL) {this.pdfIconURL = pdfIconURL;}
    }
    /*
    Our custom adapter class
     */
    public class GridViewAdapter extends BaseAdapter {
        Context c;
        ArrayList<PDFDoc> pdfDocuments;

        public GridViewAdapter(Context c, ArrayList<PDFDoc> pdfDocuments) {
            this.c = c;
            this.pdfDocuments = pdfDocuments;
        }
        @Override
        public int getCount() {return pdfDocuments.size();}
        @Override
        public Object getItem(int pos) {return pdfDocuments.get(pos);}
        @Override
        public long getItemId(int pos) {return pos;}
        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= LayoutInflater.from(c).inflate(R.layout.row_model,viewGroup,false);
            }

            TextView txtName = view.findViewById(R.id.pdfNameTxt);
            TextView txtAuthor = view.findViewById(R.id.authorTxt);
            ImageView pdfIcon = view.findViewById(R.id.imageView);


            final PDFDoc pdf= (PDFDoc) this.getItem(pos);

            txtName.setText(pdf.getName());
            txtAuthor.setText(pdf.getAuthor());

            if(pdf.getPdfURL() != null && pdf.getPdfURL().length()>0)
            {
                Picasso.get().load(pdf.getPdfIconURL()).placeholder(R.drawable.cloud).into(pdfIcon);
            }else {
                Toast.makeText(c, "Empty Image URL", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.logo).into(pdfIcon);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(c, pdf.getName(), Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(c,PDFActivity.class);
                    i.putExtra("PATH",pdf.getPdfURL());
                    c.startActivity(i);
                }
            });

            return view;
        }
    }
    /*
    Our HTTP Client
     */
    public class JSONDownloader {
        private static final String PDF_SITE_URL="https://sgh201920.000webhostapp.com/PDFViewer/PDFparserwithDate.php";
        private final Context c;
        private GridViewAdapter adapter ;

        public JSONDownloader(Context c) {
            this.c = c;
        }
        /*
        DOWNLOAD PDFS FROM MYSQL
         */
        public void retrieve(final ListView gv, final ProgressBar myProgressBar, final String Url)
        {
            final ArrayList<PDFDoc> pdfDocuments = new ArrayList<>();

            myProgressBar.setIndeterminate(true);
            myProgressBar.setVisibility(View.VISIBLE);

            AndroidNetworking.get(Url)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jo;
                            PDFDoc p;
                            try
                            {
                                for(int i=0;i<response.length();i++)
                                {
                                    jo=response.getJSONObject(i);

                                    int id=jo.getInt("id");
                                    String name=jo.getString("name");
                                    String category=jo.getString("category");
                                    String description=jo.getString("description");
                                    String pdfURL=jo.getString("pdf_url");
                                    String pdfIconURL=jo.getString("pdf_icon_url");

                                    p=new PDFDoc();
                                    p.setId(id);
                                    p.setName(name);
                                    p.setCategory(category);
                                    p.setCategory(description);
                                    p.setPdfURL(pdfURL);
                                    p.setPdfIconURL(pdfIconURL);

                                    pdfDocuments.add(p);
                                }
                                adapter =new GridViewAdapter(c,pdfDocuments);
                                gv.setAdapter(adapter);
                                myProgressBar.setVisibility(View.GONE);

                            }catch (JSONException e)
                            {
                                myProgressBar.setVisibility(View.GONE);
                                Toast.makeText(c, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIEVED. "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        //ERROR
                        @Override
                        public void onError(ANError error) {
                            error.printStackTrace();
                            myProgressBar.setVisibility(View.GONE);
                            Toast.makeText(c, "UNSUCCESSFUL :  ERROR IS : "+error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    final String defaultUrl ="https://sgh201920.000webhostapp.com/PDFViewer/pdfParser.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        final ProgressBar myProgressBar= findViewById(R.id.myProgressBar);

        final ListView myGridView= findViewById(R.id.myGridView);

        //

        final String myUrl = "https://sgh201920.000webhostapp.com/PDFViewer/pdfParser.php";
        Chip chip = findViewById(R.id.chip_year);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONDownloader(ReportActivity.this).retrieve(myGridView, myProgressBar, myUrl);
                Toast.makeText(ReportActivity.this, "Year Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONDownloader(ReportActivity.this).retrieve(myGridView, myProgressBar, defaultUrl);
                Toast.makeText(ReportActivity.this, "YearClosed Clicked", Toast.LENGTH_SHORT).show();
            }
        });




        final String myUrl1 = "https://sgh201920.000webhostapp.com/PDFViewer/pdfParser.php";
        Chip chip_month = findViewById(R.id.chip_month);
        chip_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONDownloader(ReportActivity.this).retrieve(myGridView, myProgressBar, myUrl);
                Toast.makeText(ReportActivity.this, "Month is Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        final String myUrl2 = "https://sgh201920.000webhostapp.com/PDFViewer/pdfParser.php";
        Chip chip_week = findViewById(R.id.chip_week);
        chip_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONDownloader(ReportActivity.this).retrieve(myGridView, myProgressBar, myUrl);
                Toast.makeText(ReportActivity.this, "Week is Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Button btnRetrieve= findViewById(R.id.downloadBtn);



        //new JSONDownloader(ReportActivity.this).retrieve(myGridView,myProgressBar);
        /*btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override   
            public void onClick(View view) {
                //new JSONDownloader(ReportActivity.this).retrieve(myGridView,myProgressBar);
            }
        });*/
    }
}
