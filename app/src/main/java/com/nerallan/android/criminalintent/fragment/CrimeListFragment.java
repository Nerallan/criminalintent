package com.nerallan.android.criminalintent.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.nerallan.android.criminalintent.R;
import com.nerallan.android.criminalintent.activity.CrimePagerActivity;
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

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private View mLinearLayout;
    private Button mAddCrimeButton;
    private int mAdapterPosition;
    private boolean mSubtitleVisible;


    @Override
    public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState) {
        View view = pInflater.inflate(R.layout.fragment_crime_list, pContainer, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // The LayoutManager object controls the positioning of elements and
        // determines the scrolling behavior.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLinearLayout = view.findViewById(R.id.add_crime_view);
        mAddCrimeButton = view.findViewById(R.id.add_crime_button);
        mAddCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent); //starting an instance of CrimePageActivity to edit new Crime
            }
        });

        if (pSavedInstanceState != null){
            mSubtitleVisible = pSavedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // we must explicitly tell to FragmentManager that the fragment should receive an onCreateOptionsMenu (...) call.
        // report it to FragmentManager
        setHasOptionsMenu(true);
    }


    // reload recycler view to sync updated model object with view element
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    // FragmentManager is responsible for calling Fragment.onCreateOptionsMenu (Menu, MenuInflater)
    // when activity receives an onCreateOptionsMenu callback from the OS.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        // initiate the re-creation of the toolbar when you click on the action element.
        // allows you to use the update code of the action element both when the user chooses
        // an action element and when the toolbar is re-created.
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }


    // the user selects the command in the command menu, the fragment receives the callback of the method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                // launch CrimePagerActivity from intent
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                // inform that further processing is not needed
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
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
            mAdapter.setCrimes(crimes);
            // We are updating only one item at a time so using notifyDataSetChanged would be insufficient as it updates all the items
            // so use notifyItemChanged to update only that item which has changed
            // crimeAdapter.notifyDataSetChanged();
            mAdapter.notifyItemChanged(mAdapterPosition);
        }

        // used to display button and text view if the recycler view is empty
        mLinearLayout.setVisibility(crimes.size() > 0 ? View.GONE : View.VISIBLE);
        // when creating a new crime and then returning to CrimeListActivity with the Back button,
        // update the contents of the subtitle to match the new number of crimes.
        updateSubtitle();
    }


    // set the subtitle of the toolbar with the number of crimes in CriminalIntent
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeSize = crimeLab.getCrimes().size();
        // selecting correct string depending on the number of crimes
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);
        // if subtitle is showed
        // make it hidden
        if (!mSubtitleVisible){
            subtitle = null;
        }
        // the host activity for the CrimeListFragment is converted to AppCompatActivity
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


    // ViewHolder does only one thing: it holds the View object.
    // itemView stores a reference to all representation View, transferred to super (view).
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mDeleteItemTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        // itemView stores a link to 3 view: 2 TextView and 1 CheckBox.
        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = itemView.findViewById(R.id.list_item_crime_date_text_view_);
            mSolvedCheckBox = itemView.findViewById(R.id.list_item_solved_check_box);
            mDeleteItemTextView = itemView.findViewById(R.id.delete_crime_text_view);

            mDeleteItemTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.deleteItem(getAdapterPosition());
                }
            });
        }


        // filling view items by model object
        public void bindCrime(Crime pCrime){
            mCrime = pCrime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mDeleteItemTextView.setText(R.string.delete_item);
        }


        @Override
        public void onClick(View v) {
            mAdapterPosition = getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
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


        public void setCrimes(List<Crime> pCrimes){
            mCrimes = pCrimes;
        }


        private void deleteItem(int pAdapterPosition) {
            mCrimes.remove(pAdapterPosition);
            // to update all list after removing cetraing item
            notifyDataSetChanged();
        }
    }
}
