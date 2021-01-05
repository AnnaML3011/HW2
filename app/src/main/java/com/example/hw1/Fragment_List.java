package com.example.hw1;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Collections;

public class Fragment_List extends Fragment {
    private ListView list_LST_records;
    public static final String NAME = "NAME";
    public static final String SCORE = "SCORE";
    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    private String name;
    private int score;
    private double lat= 0.0;
    private double lng = 0.0;
    private TopTenRecords tenRecords = new TopTenRecords();
    private int numOfRecords;
    private Record record = new Record();
    private FragmentLeaderBoard fragmentLeaderBoard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void findViews(View view) {
        list_LST_records = view.findViewById(R.id.list_LST_records);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list , container,false);
        findViews(view);
        initViews();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentLeaderBoard ) {
            fragmentLeaderBoard = (FragmentLeaderBoard) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    private void initViews() {
        createRecordsList();
        list_LST_records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record record2 = tenRecords.getRecords().get(position);
                    fragmentLeaderBoard.setPlayerInfo(record2);
            }
        });
    }


    private void createRecordsList(){
        Bundle bundle = this.getArguments();
        if(bundle!= null) {
                numOfRecords = bundle.getInt("numOfRecords", numOfRecords);
                if (numOfRecords > 10) {
                    numOfRecords = 10;
                }
            for (int i = 0; i < numOfRecords; i++) {
                        name = bundle.getString(NAME + i, name);
                        score = bundle.getInt(SCORE + i, score);
                        lat = bundle.getDouble(LAT+i, lat);
                        lng = bundle.getDouble(LNG+i, lng);
                        Record record = new Record(name, "", score, lat, lng);
                        tenRecords.addRecord(record);
            }
        }
        final MyAdapter adapter = new MyAdapter(getContext(), R.layout.one_record_layout, tenRecords);
        list_LST_records.setAdapter(adapter);
    }
}
