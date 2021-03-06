package com.youssif.joe.weapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youssif.joe.weapp.models.Messages;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class SectionMessageAdapter extends RecyclerView.Adapter<SectionMessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private String mSectionKey;
    private Context ctx;

    SectionMessageAdapter(List<Messages> mMessageList, String mSectionKey, Context ctx) {

        this.mMessageList = mMessageList;
        this.mSectionKey = mSectionKey;
        this.ctx = ctx;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        CircleImageView profileImage;
        TextView displayName;
        ImageView messageImage;
        TextView messageTime;

        MessageViewHolder(View view) {
            super(view);

            messageTime = view.findViewById(R.id.time_text_layout);
            messageText = view.findViewById(R.id.message_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        final Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();
        final long message_time = c.getTime();



        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("thumb_image").getValue().toString();

                viewHolder.displayName.setText(name);

                Picasso.get().load(image).placeholder(R.drawable.avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")) {

            //Convert timestamp to local device time

            SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a");
            String time = sfd.format(new Date(message_time));


            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);
            viewHolder.messageTime.setText(time);













        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);

            Picasso.get().load(c.getMessage()).placeholder(R.drawable.avatar).into(viewHolder.messageImage);

        }

        // ----------------- DELETE FEATURE --------------------
/*
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("messages")
                        .child(mSectionKey);



                final Query senderQuery = senderRef.orderByChild("time").equalTo(message_time);

                CharSequence options[] = new CharSequence[]{"Delete message"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                builder.setTitle("Select Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {

                        //Click Event for each item.
                        if(i == 0){
                            final ProgressDialog progressDialog = new ProgressDialog(ctx);
                            progressDialog.setMessage("Deleting the message for both ..");
                            progressDialog.show();

                            senderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChildren()) {
                                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                            messageSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ctx, "Message Deleted !", Toast.LENGTH_SHORT).show();
                                                    int position = viewHolder.getAdapterPosition();
                                                    mMessageList.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position,mMessageList.size());
                                                    progressDialog.dismiss();

                                                }
                                            });

                                        }
                                    }
                                    else {
                                        Toast.makeText(ctx, "Something went wrong . Please try again later !", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        }


                    }
                });

                builder.show();


                return true;
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}