package com.nerallan.android.criminalintent.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nerallan.android.criminalintent.R;
import com.nerallan.android.criminalintent.model.Crime;
import com.nerallan.android.criminalintent.model.CrimeLab;

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

    @Override
    public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState){
        View view = pInflater.inflate(R.layout.fragment_crime_list, pContainer, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // The LayoutManager object controls the positioning of elements and
        // determines the scrolling behavior.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    // configure the user interface CrimeListFragment
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    // ViewHolder does only one thing: it holds the View object.
    // itemView stores a reference to all representation View, transferred to super (view).
    private class CrimeHolder extends RecyclerView.ViewHolder {

        public TextView mTitleTextView;

        // itemView stores a link to one view: a TextView widget for the title.
        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
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
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new CrimeHolder(view);
        }

        // this method associates the View of the ViewHolder object with a model object.
        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.mTitleTextView.setText(crime.getTitle());
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
