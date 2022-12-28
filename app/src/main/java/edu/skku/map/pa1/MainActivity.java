package edu.skku.map.pa1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int check=0;
    Bitmap originalImg;
    ArrayList<Bitmap> splittedImg3, splittedImg4;
    GridView gridView;
    GridViewAdapter gridAdapter3, gridAdapter4;

    ArrayList<Bitmap> afterShuffle;
    GridViewAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        originalImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.image);
        gridView=findViewById(R.id.grid);
        ImageView imageView=findViewById(R.id.icon);

        splitImage(9);
        splitImage(16);
        gridAdapter3=new GridViewAdapter(splittedImg3, getApplicationContext(), R.layout.activity_main);
        gridAdapter4=new GridViewAdapter(splittedImg4, getApplicationContext(), R.layout.activity_main);

        gridView.setAdapter(gridAdapter3);//initial
        afterShuffle=new ArrayList<Bitmap>(9);
        for(int i=0;i<9;i++) afterShuffle.add(splittedImg3.get(i));

        //click and move
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int num=0;
                if(check==0) num=9;
                else if(check==1) num=16;

                //Toast.makeText(getApplicationContext(), position + "번째 선택", Toast.LENGTH_SHORT).show();

                int movable=movePuzzle(position);
                if(movable!=1) checkFinish();

                gameAdapter=new GridViewAdapter(afterShuffle, getApplicationContext(), R.layout.activity_main);
                gridView.setAdapter(gameAdapter);
            }
        });

        Button btn1=(Button)findViewById(R.id.button3);
        btn1.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v){
               //change grid to 3x3 initial view
               check=0;

               afterShuffle=new ArrayList<Bitmap>(9);
               for(int i=0;i<9;i++) afterShuffle.add(splittedImg3.get(i));

               gridView.setNumColumns(3);
               gridView.setAdapter(gridAdapter3);
           }
        });

        Button btn2=(Button)findViewById(R.id.button4);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //change grid to 4x4 initial view
                check=1;

                afterShuffle=new ArrayList<Bitmap>(16);
                for(int i=0;i<16;i++) afterShuffle.add(splittedImg4.get(i));

                gridView.setNumColumns(4);
                gridView.setAdapter(gridAdapter4);
            }
        });

        Button btn3=(Button)findViewById(R.id.shuffle_button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int num=0;
                if(check==0) num=9;
                else if(check==1) num=16;

                //shuffle puzzles
                afterShuffle=new ArrayList<Bitmap>(num);
                int i;
                int cnt=num;
                int[] cntArr=new int[num];
                for(int j=0;j<num;j++) cntArr[j]=0;
                long seed = System.currentTimeMillis();
                Random rand = new Random(seed);
                if(check==0) {
                    while(cnt>0) {
                        i= rand.nextInt(num);
                        if(cntArr[i]!=1) {
                            afterShuffle.add(splittedImg3.get(i));
                            cntArr[i]=1;
                            cnt--;
                        }
                    }
                }
                else if(check==1){
                    while(cnt>0) {
                        i= rand.nextInt(num);
                        if(cntArr[i]!=1) {
                            afterShuffle.add(splittedImg4.get(i));
                            cntArr[i]=1;
                            cnt--;
                        }
                    }
                }

                gameAdapter=new GridViewAdapter(afterShuffle, getApplicationContext(), R.layout.activity_main);
                gridView.setAdapter(gameAdapter);
            }
        });
    }


    class GridViewAdapter extends BaseAdapter {
        ArrayList<Bitmap> items;
        Context context;
        LayoutInflater inflater;

        public GridViewAdapter(ArrayList<Bitmap> objects, Context ctx, int textViewResourceId){
            this.items=objects;
            this.context=ctx;
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView=null;

            if(convertView==null){
                if(check==0) convertView=inflater.inflate(R.layout.activity_gridview, parent, false);
                else if(check==1) convertView=inflater.inflate(R.layout.activity_gridview1, parent, false);
            }

            imageView=(ImageView)convertView.findViewById(R.id.icon);
            imageView.setImageBitmap(items.get(position));

            return imageView;
        }
    }

    private void splitImage(int num){

        int row, col;
        row=col=(int)Math.sqrt(num);

        int splittedHeight, splittedWidth;
        splittedHeight=originalImg.getHeight()/row;
        splittedWidth=originalImg.getWidth()/col;

        if(num==9) splittedImg3=new ArrayList<Bitmap>(num);
        else if(num==16) splittedImg4=new ArrayList<Bitmap>(num);


        int xCoor, yCoor=0;
        for(int i=0; i <row; i++){
            xCoor=0;
            for(int j=0; j<col; j++){
                if(num==9) {
                    splittedImg3.add(originalImg.createBitmap(originalImg, xCoor, yCoor, splittedWidth, splittedHeight));
                }
                else if(num==16) {
                    splittedImg4.add(originalImg.createBitmap(originalImg, xCoor, yCoor, splittedWidth, splittedHeight));
                }

                xCoor+=splittedWidth;
            }

            yCoor+=splittedHeight;
        }

        if(num==9) {
            splittedImg3.remove(num-1);
            splittedImg3.add(null);
        }
        else if(num==16) {
            splittedImg4.remove(num-1);
            splittedImg4.add(null);
        }
    }

    public int movePuzzle(int position){
        int movable=0;//can move
        Bitmap tmp;
        if(check==0){
            switch(position){
                case 0:
                    tmp=afterShuffle.get(0);
                    if(afterShuffle.get(1)==null) {
                        afterShuffle.set(1, tmp);
                        afterShuffle.set(0, null);
                    }
                    else if(afterShuffle.get(3)==null){
                        afterShuffle.set(3, tmp);
                        afterShuffle.set(0, null);
                    }
                    else movable=1;//can't move
                    break;
                case 2:
                    tmp=afterShuffle.get(2);
                    if(afterShuffle.get(1)==null) {
                        afterShuffle.set(1, tmp);
                        afterShuffle.set(2, null);
                    }
                    else if(afterShuffle.get(5)==null){
                        afterShuffle.set(5, tmp);
                        afterShuffle.set(2, null);
                    }
                    else movable=1;
                    break;
                case 6:
                    tmp=afterShuffle.get(6);
                    if(afterShuffle.get(3)==null) {
                        afterShuffle.set(3, tmp);
                        afterShuffle.set(6, null);
                    }
                    else if(afterShuffle.get(7)==null){
                        afterShuffle.set(7, tmp);
                        afterShuffle.set(6, null);
                    }
                    else movable=1;
                    break;
                case 8:
                    tmp=afterShuffle.get(8);
                    if(afterShuffle.get(5)==null) {
                        afterShuffle.set(5, tmp);
                        afterShuffle.set(8, null);
                    }
                    else if(afterShuffle.get(7)==null){
                        afterShuffle.set(7, tmp);
                        afterShuffle.set(8, null);
                    }
                    else movable=1;
                    break;
                case 1:
                    tmp=afterShuffle.get(1);
                    if(afterShuffle.get(0)==null) {
                        afterShuffle.set(0, tmp);
                        afterShuffle.set(1, null);
                    }
                    else if(afterShuffle.get(2)==null){
                        afterShuffle.set(2, tmp);
                        afterShuffle.set(1, null);
                    }
                    else if(afterShuffle.get(4)==null){
                        afterShuffle.set(4, tmp);
                        afterShuffle.set(1, null);
                    }
                    else movable=1;
                    break;
                case 3:
                    tmp=afterShuffle.get(3);
                    if(afterShuffle.get(0)==null) {
                        afterShuffle.set(0, tmp);
                        afterShuffle.set(3, null);
                    }
                    else if(afterShuffle.get(4)==null){
                        afterShuffle.set(4, tmp);
                        afterShuffle.set(3, null);
                    }
                    else if(afterShuffle.get(6)==null){
                        afterShuffle.set(6, tmp);
                        afterShuffle.set(3, null);
                    }
                    else movable=1;
                    break;
                case 5:
                    tmp=afterShuffle.get(5);
                    if(afterShuffle.get(2)==null) {
                        afterShuffle.set(2, tmp);
                        afterShuffle.set(5, null);
                    }
                    else if(afterShuffle.get(4)==null){
                        afterShuffle.set(4, tmp);
                        afterShuffle.set(5, null);
                    }
                    else if(afterShuffle.get(8)==null){
                        afterShuffle.set(8, tmp);
                        afterShuffle.set(5, null);
                    }
                    else movable=1;
                    break;
                case 7:
                    tmp=afterShuffle.get(7);
                    if(afterShuffle.get(4)==null) {
                        afterShuffle.set(4, tmp);
                        afterShuffle.set(7, null);
                    }
                    else if(afterShuffle.get(6)==null){
                        afterShuffle.set(6, tmp);
                        afterShuffle.set(7, null);
                    }
                    else if(afterShuffle.get(8)==null){
                        afterShuffle.set(8, tmp);
                        afterShuffle.set(7, null);
                    }
                    else movable=1;
                    break;
                case 4:
                    tmp=afterShuffle.get(4);
                    if(afterShuffle.get(1)==null) {
                        afterShuffle.set(1, tmp);
                        afterShuffle.set(4, null);
                    }
                    else if(afterShuffle.get(3)==null){
                        afterShuffle.set(3, tmp);
                        afterShuffle.set(4, null);
                    }
                    else if(afterShuffle.get(5)==null){
                        afterShuffle.set(5, tmp);
                        afterShuffle.set(4, null);
                    }
                    else if(afterShuffle.get(7)==null){
                        afterShuffle.set(7, tmp);
                        afterShuffle.set(4, null);
                    }
                    else movable=1;
                    break;
            }
        }

        else if(check==1){
            switch(position){
                case 0:
                    tmp=afterShuffle.get(0);
                    if(afterShuffle.get(1)==null) {
                        afterShuffle.set(1, tmp);
                        afterShuffle.set(0, null);
                    }
                    else if(afterShuffle.get(4)==null){
                        afterShuffle.set(4, tmp);
                        afterShuffle.set(0, null);
                    }
                    else movable=1;
                    break;
                case 3:
                    tmp=afterShuffle.get(3);
                    if(afterShuffle.get(2)==null) {
                        afterShuffle.set(2, tmp);
                        afterShuffle.set(3, null);
                    }
                    else if(afterShuffle.get(7)==null){
                        afterShuffle.set(7, tmp);
                        afterShuffle.set(3, null);
                    }
                    else movable=1;
                    break;
                case 12:
                    tmp=afterShuffle.get(12);
                    if(afterShuffle.get(8)==null) {
                        afterShuffle.set(8, tmp);
                        afterShuffle.set(12, null);
                    }
                    else if(afterShuffle.get(13)==null){
                        afterShuffle.set(13, tmp);
                        afterShuffle.set(12, null);
                    }
                    else movable=1;
                    break;
                case 15:
                    tmp=afterShuffle.get(15);
                    if(afterShuffle.get(11)==null) {
                        afterShuffle.set(11, tmp);
                        afterShuffle.set(15, null);
                    }
                    else if(afterShuffle.get(14)==null){
                        afterShuffle.set(14, tmp);
                        afterShuffle.set(15, null);
                    }
                    else movable=1;
                    break;
                case 1:
                    tmp=afterShuffle.get(1);
                    if(afterShuffle.get(0)==null) {
                        afterShuffle.set(0, tmp);
                        afterShuffle.set(1, null);
                    }
                    else if(afterShuffle.get(2)==null){
                        afterShuffle.set(2, tmp);
                        afterShuffle.set(1, null);
                    }
                    else if(afterShuffle.get(5)==null){
                        afterShuffle.set(5, tmp);
                        afterShuffle.set(1, null);
                    }
                    else movable=1;
                    break;
                case 2:
                    tmp=afterShuffle.get(2);
                    if(afterShuffle.get(1)==null) {
                        afterShuffle.set(1, tmp);
                        afterShuffle.set(2, null);
                    }
                    else if(afterShuffle.get(3)==null){
                        afterShuffle.set(3, tmp);
                        afterShuffle.set(2, null);
                    }
                    else if(afterShuffle.get(6)==null){
                        afterShuffle.set(6, tmp);
                        afterShuffle.set(2, null);
                    }
                    else movable=1;
                    break;
                case 4:
                    tmp=afterShuffle.get(4);
                    if(afterShuffle.get(0)==null) {
                        afterShuffle.set(0, tmp);
                        afterShuffle.set(4, null);
                    }
                    else if(afterShuffle.get(5)==null){
                        afterShuffle.set(5, tmp);
                        afterShuffle.set(4, null);
                    }
                    else if(afterShuffle.get(8)==null){
                        afterShuffle.set(8, tmp);
                        afterShuffle.set(4, null);
                    }
                    else movable=1;
                    break;
                case 7:
                    tmp=afterShuffle.get(7);
                    if(afterShuffle.get(3)==null) {
                        afterShuffle.set(3, tmp);
                        afterShuffle.set(7, null);
                    }
                    else if(afterShuffle.get(6)==null){
                        afterShuffle.set(6, tmp);
                        afterShuffle.set(7, null);
                    }
                    else if(afterShuffle.get(11)==null){
                        afterShuffle.set(11, tmp);
                        afterShuffle.set(7, null);
                    }
                    else movable=1;
                    break;
                case 8:
                    tmp=afterShuffle.get(8);
                    if(afterShuffle.get(4)==null) {
                        afterShuffle.set(4, tmp);
                        afterShuffle.set(8, null);
                    }
                    else if(afterShuffle.get(9)==null){
                        afterShuffle.set(9, tmp);
                        afterShuffle.set(8, null);
                    }
                    else if(afterShuffle.get(12)==null){
                        afterShuffle.set(12, tmp);
                        afterShuffle.set(8, null);
                    }
                    else movable=1;
                    break;
                case 11:
                    tmp=afterShuffle.get(11);
                    if(afterShuffle.get(7)==null) {
                        afterShuffle.set(7, tmp);
                        afterShuffle.set(11, null);
                    }
                    else if(afterShuffle.get(10)==null){
                        afterShuffle.set(10, tmp);
                        afterShuffle.set(11, null);
                    }
                    else if(afterShuffle.get(15)==null){
                        afterShuffle.set(15, tmp);
                        afterShuffle.set(11, null);
                    }
                    else movable=1;
                    break;
                case 13:
                    tmp=afterShuffle.get(13);
                    if(afterShuffle.get(9)==null) {
                        afterShuffle.set(9, tmp);
                        afterShuffle.set(13, null);
                    }
                    else if(afterShuffle.get(12)==null){
                        afterShuffle.set(12, tmp);
                        afterShuffle.set(13, null);
                    }
                    else if(afterShuffle.get(14)==null){
                        afterShuffle.set(14, tmp);
                        afterShuffle.set(13, null);
                    }
                    else movable=1;
                    break;
                case 14:
                    tmp=afterShuffle.get(14);
                    if(afterShuffle.get(10)==null) {
                        afterShuffle.set(10, tmp);
                        afterShuffle.set(14, null);
                    }
                    else if(afterShuffle.get(13)==null){
                        afterShuffle.set(13, tmp);
                        afterShuffle.set(14, null);
                    }
                    else if(afterShuffle.get(15)==null){
                        afterShuffle.set(15, tmp);
                        afterShuffle.set(14, null);
                    }
                    else movable=1;
                    break;
                case 5:
                    tmp=afterShuffle.get(5);
                    if(afterShuffle.get(1)==null) {
                        afterShuffle.set(1, tmp);
                        afterShuffle.set(5, null);
                    }
                    else if(afterShuffle.get(4)==null){
                        afterShuffle.set(4, tmp);
                        afterShuffle.set(5, null);
                    }
                    else if(afterShuffle.get(6)==null){
                        afterShuffle.set(6, tmp);
                        afterShuffle.set(5, null);
                    }
                    else if(afterShuffle.get(9)==null){
                        afterShuffle.set(9, tmp);
                        afterShuffle.set(5, null);
                    }
                    else movable=1;
                    break;
                case 6:
                    tmp=afterShuffle.get(6);
                    if(afterShuffle.get(2)==null) {
                        afterShuffle.set(2, tmp);
                        afterShuffle.set(6, null);
                    }
                    else if(afterShuffle.get(5)==null){
                        afterShuffle.set(5, tmp);
                        afterShuffle.set(6, null);
                    }
                    else if(afterShuffle.get(7)==null){
                        afterShuffle.set(7, tmp);
                        afterShuffle.set(6, null);
                    }
                    else if(afterShuffle.get(10)==null){
                        afterShuffle.set(10, tmp);
                        afterShuffle.set(6, null);
                    }
                    else movable=1;
                    break;
                case 9:
                    tmp=afterShuffle.get(9);
                    if(afterShuffle.get(5)==null) {
                        afterShuffle.set(5, tmp);
                        afterShuffle.set(9, null);
                    }
                    else if(afterShuffle.get(8)==null){
                        afterShuffle.set(8, tmp);
                        afterShuffle.set(9, null);
                    }
                    else if(afterShuffle.get(10)==null){
                        afterShuffle.set(10, tmp);
                        afterShuffle.set(9, null);
                    }
                    else if(afterShuffle.get(13)==null){
                        afterShuffle.set(13, tmp);
                        afterShuffle.set(9, null);
                    }
                    else movable=1;
                    break;
                case 10:
                    tmp=afterShuffle.get(10);
                    if(afterShuffle.get(6)==null) {
                        afterShuffle.set(6, tmp);
                        afterShuffle.set(10, null);
                    }
                    else if(afterShuffle.get(9)==null){
                        afterShuffle.set(9, tmp);
                        afterShuffle.set(10, null);
                    }
                    else if(afterShuffle.get(11)==null){
                        afterShuffle.set(11, tmp);
                        afterShuffle.set(10, null);
                    }
                    else if(afterShuffle.get(14)==null){
                        afterShuffle.set(14, tmp);
                        afterShuffle.set(10, null);
                    }
                    else movable=1;
                    break;
            }
        }

        return movable;
    }

    public void checkFinish(){
        int num=0;
        if(check==0) num=9;
        else if(check==1) num=16;

        int i=0;
        int finish=0;
        if(check==0){
            while(i<num){
                if(afterShuffle.get(i)!=splittedImg3.get(i)) break;
                else i++;
                if(i==num-1) finish=1;
            }
        }
        else if(check==1){
            while(i<num){
                if(afterShuffle.get(i)!=splittedImg4.get(i)) break;
                else i++;
                if(i==num-1) finish=1;
            }
        }

        if(finish==1) Toast.makeText(getApplicationContext(), "FINISH!", Toast.LENGTH_LONG).show();
    }
}