package radimbures.firstaidlog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private  List<Bitmap> fotky;
    private List<String> cesty;
    private Bitmap foto;
    private String cesta;
    private int outWidth;
    private int outHeight;
    private int inWidth;
    private int inHeight;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        foto = fotky.get(position);
        cesta = cesty.get(position);
        final int maxSize = 300;
        inWidth = foto.getWidth();
        inHeight = foto.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                foto, outWidth, outHeight, false);
        holder.photo.setImageBitmap(resizedBitmap);
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder imageDialog = new AlertDialog.Builder(view.getContext());
                imageDialog.setTitle("Image");
                //View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_image, (ViewGroup) view,false);
                View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_image, null);
                imageDialog.setView(viewInflated);
                ImageView image = viewInflated.findViewById(R.id.fotka);
                foto = fotky.get(position);
                final int maxSize = 850;
                inWidth = foto.getWidth();
                inHeight = foto.getHeight();
                if(inWidth > inHeight){
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }

                Bitmap resizedFoto = Bitmap.createScaledBitmap(
                        foto, outWidth, outHeight, false);
                image.setImageBitmap(resizedFoto);
                imageDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                imageDialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cesta = cesty.get(position);
                        File file = new File(cesta);
                        boolean deleted = file.delete();
                        remove(position);
                    }
                });
                imageDialog.show();
            }



        });

    }

    public void add(int position, Bitmap item) {
        fotky.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        fotky.remove(position);
        cesty.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, fotky.size());

    }

    public Adapter(List<Bitmap> myDataset, List<String> myDataset2) {
        fotky = myDataset;
        cesty = myDataset2;
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
