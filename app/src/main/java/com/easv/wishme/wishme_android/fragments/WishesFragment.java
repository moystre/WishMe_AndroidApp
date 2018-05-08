package com.easv.wishme.wishme_android.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.easv.wishme.wishme_android.R;
import com.easv.wishme.wishme_android.adapters.WishAdapter;
import com.easv.wishme.wishme_android.entities.User;
import com.easv.wishme.wishme_android.entities.Wish;
import com.easv.wishme.wishme_android.entities.Wishlist;
import com.easv.wishme.wishme_android.utils.EditWishlistDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WishesFragment extends android.support.v4.app.Fragment {


    private static final String TAG = "WishesFragment";
    public ListView mWishList;
    public WishAdapter wishAdapter;
    private TextView mNoWishes, mNameOfWishlist;
    private ArrayList<Wish> wishListList;
    private Wishlist listFromHome;
    private FloatingActionButton mAddWish;
    private FirebaseFirestore db;
    private Toolbar toolbar;

    public interface OnEditWishList{
        void getWishlist(Wishlist wList);

    }
    WishesFragment.OnEditWishList mOnEditWishList;

    public WishesFragment() {
        super();
        setArguments(new Bundle());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        mNoWishes = view.findViewById(R.id.textNoWishes);
        mWishList = view.findViewById(R.id.wishesList);
        mNameOfWishlist = view.findViewById(R.id.nameOfWishlist);
        mAddWish = (FloatingActionButton) view.findViewById(R.id.addWishFab);
        db = FirebaseFirestore.getInstance();
        toolbar = view.findViewById(R.id.wishlistToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        listFromHome = getWishListFromBundle();
        mNameOfWishlist.setText(listFromHome.getwListName());
        Log.d(TAG, listFromHome.getwListName());
        setHasOptionsMenu(true);
        mAddWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addWish();
            }
        });
        return view;
    }

    private void addWish(){
        AddWishFragment fragment = new AddWishFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Wishlist getWishListFromBundle(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getParcelable("WishList");
        } else {
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mNoWishes.setText("Loading...");
        wishListList = new ArrayList<>();
        db.collection("wish").whereEqualTo("owner", listFromHome.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mNoWishes.setText("");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Wish wish = document.toObject(Wish.class);
                                wishListList.add(wish);
                            }
                            wishAdapter = new WishAdapter(getActivity(), R.layout.wish_item, wishListList, "https://");
                            mWishList.setAdapter(wishAdapter);
                            sortListByName();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            mNoWishes.setText("Something went wrong :(");
                        }
                    }
                });
    }

    private void sortListByName() {
        Collections.sort(wishListList, new Comparator<Wish>() {
            @Override
            public int compare(Wish o1, Wish o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }


    private void checkIfChanged() {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wishlist_menu, menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuitem_edit_wishlist:
                mOnEditWishList.getWishlist(listFromHome);
                return true;
            case R.id.menuitem_delet_wishlist:
              //  editProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnEditWishList = (WishesFragment.OnEditWishList) getActivity();

        }catch(ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " +  e.getMessage());

        }
    }





}
