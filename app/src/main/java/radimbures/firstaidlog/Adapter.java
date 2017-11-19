package radimbures.firstaidlog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private  List<Bitmap> fotky;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Bitmap foto = fotky.get(position);
        holder.photo.setImageBitmap(foto);
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO tady ukázat full image s možností smazání
                remove(position);
            }
        });

    }

    public void add(int position, Bitmap item) {
        fotky.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        fotky.remove(position);
        notifyItemRemoved(position);
    }

    public Adapter(List<Bitmap> myDataset) {
        fotky = myDataset;
    }


    @Override
    public int getItemCount() {
        return fotky.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView);
        }
    }

}
