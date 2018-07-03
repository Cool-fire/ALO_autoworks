package com.aloautoworks.alo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aloautoworks.alo.models.ServiceCenter;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static javax.mail.Session.getDefaultInstance;

public class ServiceCenterActivity extends AppCompatActivity {

    String[] Names={"CAR WORLD AUTO",
            "TURBO MOTORS",
            "AUTORAM MOTORS",
            "HAYAR CAR CARE",
            "ANAND MOTOES",
            "HANEET MOTOR WORKS",
            "HARJIT MOTORS",
            "RD MOTORS",
            "VARDHMAN AUTO SERVICE CENTRE",
            "CARPOD SHAILI MOTORS",
            "DIAMOND STEAM CAR WASH",
            "XENON AUTO CARE",
            "DASHMESH CAR CARE",
            "MODERN AUTOMOBILES",
            "CAR CLINIC",
            "SALUJA MOTORS PRIVATE LIMITED",
            "SPEED CAR CARE",
            "WOW CARS"};
    String[] Address={"PLOT NO 45,Chandigarh  Road Phase 1,Industrial Area,Chandigarh -160002, NEAR HDFC BANK (HEAD OFFICE)",
            "Plot number 724,Sector 82,Mohali,Chandigarh 160062, JLPL",
            "Plot No. 575,JLPL, Sector 82,Chandigarh 140308, Near Gurudwara Sahib",
            "Plot No. B-56,Industrial Area Phase 6, Mohali,Chandigarh 160055, Near ISBT Mohali",
            "Main Road, Vip Road, Zirakpur HO, chandigarh 140603, Opposite Maya Gardens Phase 1",
            "Plot-126,Industrial tArea,Phase 9, Mohali,Phase-9, Mohali,Chandigarh 160062, Near Philips Factory",
            "Ramdarbar,Industrial Area Phase II,Chandigarh  160002, Near Punjab National Bank Opposite To Plot 934",
            "Makhan Majra Road, Chandigarh - 160017, Near Airport Chowk",
            "plot 1054 jlpl airport road,Mohali sector 82,Chandigarh 160062",
            "Plot Number 270,chandigarh 134109",
            "Plot No. 589,Industrial Area,Mohali, Sector 66, Chandigarh 160062, Near",
            "Shop Number 2,VIP ROAD, Chandigarh 140603, OPP Maya Garden",
            "Booth No-32 Block C,Chandigarh Sector 32c, Chandigarh 160031, Near Axis Bank",
            "Plot Number - 4 MW, Industrial Area Phase I,Chandigarh 160002, Near Hotel Lemon Tree",
            "Plot no-128,Industrial Area,Chandigarh 160002, Phase-2",
            "Plot Number 140,Industrial Area Phase II,Chandigarh 160002, Near Govind Sweets",
            "Plot No- 512,Airport Road,Mohali,Chandigarh 160062, Near JLPL Office",
            "SCO - 15 & 16,Chandigarh Road,Zirakpur,Chandigarh,140603, Opposite Ranjan Plaza"};
    String[] numbers={"+91-6203591301",
            "+91-9631624296",
            "+91-9801497023",
            "+91-7903199545",
            "+91-909772049",
            "+91-9007776712",
            "+91-9724422686",
            "+91-9149904054",
            "+91-7887003032",
            "+91-9560528081",
            "+91-7889836796",
            "+91-9308749638",
            "+91-8859333192",
            "+91-7091303806",
            "+91-9122885469",
            "+91-7717758246",
            "+91-8178857146",
            "+91-7050009858"};
    String[] email={"adarsh3110r@gmail.com",
            "",
            "",
            "vishwashankitkumar8@gmail.com",
            "",
            "abhishekdugros@gmail.com",
            "sapoliakaran@gmail.com",
            "sravi4701@gmail.com",
            "14bcs039@smvdu.ac.in",
            "",
            "kumarabhijeet035@gmail.com",
            "ankur19925536@gmail.com",
            "",
            "nishantheart.13@gmail.com",
            "ksanjay31@rediffmail.com",
            "abhaswithdelrio@gmail.com",
            "kumarritesh35@gmail.com",
            ""
    };
    private AlertDialog dialog;
    private AlertDialog dialog1;
    private String subject;
    private String message;
    private String vehiclename;
    private String servicetype;
    private String modelno;
    private String pincodeno;
    private String mileageno;
    private String subserviceno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quotes");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorwhite));


        Intent intent = getIntent();
        vehiclename = intent.getStringExtra("vehiclename");
        servicetype = intent.getStringExtra("servicetype");
        modelno = intent.getStringExtra("modelno");
        pincodeno = intent.getStringExtra("pincodeno");
        mileageno = intent.getStringExtra("mileageno");
        subserviceno = intent.getStringExtra("subservice");

        ListView listView=(ListView)findViewById(R.id.list);
        CustomAdapter customAdapter=new CustomAdapter();
        listView.setAdapter(customAdapter);

    }

    class CustomAdapter extends BaseAdapter {

        private TextView centerName;
        private TextView centeraddress;
        private TextView centernumber;
        private TextView centeremail;
        private Button bookButton;

        @Override
        public int getCount() {
            return Names.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }



        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.servicecenterrow,null);
            centerName = (TextView) view.findViewById( R.id.centerName);
            centeraddress = (TextView)view.findViewById( R.id.centerAddress);
            centernumber = (TextView)view.findViewById( R.id.centerNumber);
            centeremail = (TextView)view.findViewById( R.id.centerEmail);
            bookButton = (Button)view.findViewById(R.id.bookButton);

            centerName.setText(Names[i]);
            centeraddress.setText(Address[i]);
            centernumber.setText("Quote : 5000");
            centeremail.setText(email[i]);

            bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sendEmail(centerName);
                //    sendEmaimethod();
                }

            });

            return view;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if(dialog !=null )
        {
            if(dialog.isShowing())
            {
                dialog.dismiss();
            }
        }

        if(dialog1!=null)
        {
            if(dialog1.isShowing())
            {
                dialog1.dismiss();

            }
        }




    }

    private void sendEmail(TextView centerName) {
        subject = "Regarding your booking at aloautoworks";
        message = "You have a quote from customer with details"+"\n"+vehiclename+"\n"+modelno+"\n"+servicetype+"\n"+pincodeno+"\n"+mileageno+"\n"+subserviceno;
        Log.d("TAG", "sendEmail: "+message);
        SendMail sm = new SendMail(this, "upendrareddy2511@gmail.com", subject, message);

        //Executing sendmail to send email
        sm.execute();
        Log.d("TAG", "sendEmail: "+ sm.getStatus());
    }

    private void sendEmaimethod() {



        //new sendEmailTask().execute("sainathreddy.k16@iiits.in","upendrareddy2511@gmail.com");
        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceCenterActivity.this);
        LayoutInflater inflater = ServiceCenterActivity.this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialoglayout, null))
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showcancelDialog();
                    }
                })
                // Add action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }

                });

        dialog = builder.create();
        dialog.show();
    }

    private void showcancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceCenterActivity.this);
        builder.setMessage("Are you sure to canel Booking").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog1 = builder.create();
        dialog1.show();

    }


    public class SendMail extends AsyncTask<Void,Void,Void> {

        //Declaring Variables
        private Context context;
        private Session session;

        //Information to send email
        private String email;
        private String subject;
        private String message;

        //Progressdialog to show while sending email
        private ProgressDialog progressDialog;

        //Class Constructor
        public SendMail(Context context, String email, String subject, String message){
            //Initializing variables
            this.context = context;
            this.email = email;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing progress dialog while sending email
            progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Dismissing the progress dialog
            progressDialog.dismiss();
            Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
            sendEmaimethod();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Creating properties
            Properties props = new Properties();

            //Configuring properties for gmail
            //If you are not using gmail you may need to change the values
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            //Creating a new session
            session = getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        //Authenticating the password
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("aloautoworks@gmail.com", "123456789aloautoworks");
                        }
                    });

            try {
                //Creating MimeMessage object
                MimeMessage mm = new MimeMessage(session);

                //Setting sender address
                mm.setFrom(new InternetAddress("aloautoworks@gmail.com"));
                //Adding receiver
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                //Adding subject
                mm.setSubject(subject);
                //Adding message
                mm.setText(message);

                //Sending email
                Transport.send(mm);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



}
