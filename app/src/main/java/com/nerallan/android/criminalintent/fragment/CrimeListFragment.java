package com.nerallan.android.criminalintent.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.nerallan.android.criminalintent.R;
import com.nerallan.android.criminalintent.activity.CrimeActivity;
import com.nerallan.android.criminalintent.model.Crime;
import com.nerallan.android.criminalintent.model.CrimeLab;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Nerallan on 10/8/2018.
 */

// RecyclerView in subclass of ViewGroup
// it displays a list of child objects of View, one for each element
// The only responsibility of the RecyclerView is to reuse TextView widgets and position them on the screen.
// When a RecyclerView widget needs a view object to display, it enters into a dialogue with its adapter.
public class CrimeListFragment extends Fragment{

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mAdapterPosition;

    @Override
    public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState) {
        View view = pInflater.inflate(R.layout.fragment_crime_list, pContainer, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // The LayoutManager object controls the positioning of elements and
        // determines the scrolling behavior.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    // reload recycler view to sync updated model object with view element
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    // configure the user interface CrimeListFragment
    // binding adapter and RecyclerView
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        // if object CrimeAdapter not created yet
        if (mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyItemChanged(mAdapterPosition);
        }
    }

    // ViewHolder does only one thing: it holds the View object.
    // itemView stores a reference to all representation View, transferred to super (view).
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        // itemView stores a link to 3 view: 2 TextView and 1 CheckBox.
        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = itemView.findViewById(R.id.list_item_crime_date_text_view_);
            mSolvedCheckBox = itemView.findViewById(R.id.list_item_solved_check_box);
        }

        // filling view items by model object
        public void bindCrime(Crime pCrime){
            mCrime = pCrime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            mAdapterPosition = getAdapterPosition();
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    // adapter - to the controller object that is between the RecyclerView and the dataset with
    // the information that RecyclerView should display
    // The adapter is responsible for:
    // creating the necessary ViewHolder objects;
    // associating the ViewHolder with data from the model level
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> pCrimes){
            mCrimes = pCrimes;
        }

        // method is called by RecyclerView
        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // fill in the layout of the standard Android library named simple_list_item_1.
            // This layout contains one TextView widget designed to look good in the list.
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        // this method associates the View of the ViewHolder object with a model object by position
        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
