package com.example.sunray.mergedproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private static List<Cell> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public GridAdapter(Context aContext, List<Cell> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = layoutInflater.from(aContext);
    }

    public GridAdapter(Context aContext) {
        this.context = aContext;
        layoutInflater = layoutInflater.from(aContext);
    }

    public void setListData(List<Cell> list) {
        listData = list;
    }

    public void setItem(Integer index, Cell cell) {
        listData.set(index, cell);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_item_layout, null);
            holder = new ViewHolder();
            holder.cellView = (ImageView) convertView.findViewById(R.id.imageView_cell);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cell cell = this.listData.get(position);
        int imageid = this.getMipmapResIdByName(cell.getImageName());
        holder.cellView.setImageResource(imageid);
        return convertView;
    }

    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();

        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomGridView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    static class ViewHolder {
        ImageView cellView;
    }
}
