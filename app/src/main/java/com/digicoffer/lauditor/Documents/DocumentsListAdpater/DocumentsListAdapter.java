package com.digicoffer.lauditor.Documents.DocumentsListAdpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digicoffer.lauditor.Documents.models.DocumentsModel;
import com.digicoffer.lauditor.Groups.Adapters.GroupAdapters;
import com.digicoffer.lauditor.Groups.GroupModels.GroupModel;
import com.digicoffer.lauditor.R;

import java.util.ArrayList;

public class DocumentsListAdapter extends  RecyclerView.Adapter<DocumentsListAdapter.ViewHolder> {
    ArrayList<DocumentsModel> itemsArrayList;

    public DocumentsListAdapter(ArrayList<DocumentsModel> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }


    @NonNull
    @Override
    public DocumentsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.displays_documents_list, parent, false);
        return new DocumentsListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsListAdapter.ViewHolder holder, int position) {
        DocumentsModel documentsModel = itemsArrayList.get(position);
        holder.tv_document_name.setText(documentsModel.getName());
        holder.cb_documents_list.setChecked(itemsArrayList.get(position).isChecked());
        holder.cb_documents_list.setTag(position);
//            if(groupModel.isIsenabled()==null)
        if (itemsArrayList.get(position).isIsenabled()){
            holder.cb_documents_list.setEnabled(true);
        }else
        {
            holder.cb_documents_list.setEnabled(false);
        }
        holder.cb_documents_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = (Integer) holder.cb_documents_list.getTag();
                if (itemsArrayList.get(pos).isChecked()) {
                    itemsArrayList.get(pos).setChecked(false);

//                        itemsArrayList.add(itemsArrayList.get(pos));
                } else {
                    itemsArrayList.get(pos).setChecked(true);
//                        itemsArrayList.remove(itemsArrayList.get(pos));
                }


            }
        });
//            holder.cb_team_members.setChecked(true);
//        holder.tv_tm_name.setText(groupModel.getName());
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_documents_list;
        private TextView tv_document_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb_documents_list = itemView.findViewById(R.id.chk_selected_documents);
            tv_document_name = itemView.findViewById(R.id.tv_document_name);
        }
    }
}