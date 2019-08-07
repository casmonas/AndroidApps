package com.sarch.travelmantic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ListActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

      // Enabling Offline Persistence
      FirebaseUtil.getDatabase();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.list_activity_menu, menu);
    MenuItem insertMenu = menu.findItem(R.id.insert_menu);
    if (FirebaseUtil.isAdmin) {
      insertMenu.setVisible(true);
    }
    else {
      insertMenu.setVisible(false);
    }

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.insert_menu:
          startActivity(new Intent(ListActivity.this, DealsActivity.class));
        return true;
      case R.id.logout_menu:
          FirebaseUtil.signOut();
          FirebaseUtil.detachListener();
          return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPause() {
    super.onPause();
    FirebaseUtil.detachListener();
    showMenu();
  }

  @Override
  protected void onResume() {
    super.onResume();
    FirebaseUtil.openFbReference("traveldeals", this);
    RecyclerView rvDeals = findViewById(R.id.rvDeals);
    final DealAdapter adapter = new DealAdapter();
    rvDeals.setAdapter(adapter);
      rvDeals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
      FirebaseUtil.attachListener();
      if (FirebaseUtil.mFirebaseAuth.getUid() != null) {
          FirebaseUtil.checkAdmin(FirebaseUtil.mFirebaseAuth.getUid());
          showMenu();
      }
  }

  public void showMenu() {
    invalidateOptionsMenu();
  }
}
