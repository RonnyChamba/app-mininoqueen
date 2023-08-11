package com.app.mininoqueen.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.modelos.Product;

import java.util.List;

public class AdapterCourse extends RecyclerView.Adapter<AdapterCourse.ViewHolder> implements

        View.OnClickListener {

    private List<Product> courses;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    public AdapterCourse(
            Context context,
            List<Product> courses) {

        this.inflater = LayoutInflater.from(context);
        this.courses = courses;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_products, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        List<Product> itemCourse = courses.get(position);
        Product itemCourse = courses.get(position);
        holder.setData(itemCourse);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setOnClickListener(View.OnClickListener lister
    ) {

        this.listener = lister;
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {
            listener.onClick(v);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // aqui se referencia los widgets

        private final TextView textTitle;
        private final TextView textDescription;
        private final ImageView imageProduct;

        private final CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.item_title);
            textDescription = itemView.findViewById(R.id.item_description);
            imageProduct = itemView.findViewById(R.id.imageViewProduct);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void setData(Product course) {


            // agagrear margin inferior
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            // si el indice del item es par, se pone un color de fondo, si es impar, se pone otro color de fondo
            if (getAdapterPosition() % 2 == 0) {

                params.setMargins(5, 0, 10, 10);
            } else {

                params.setMargins(0, 0, 0, 10);
            }

            cardView.setLayoutParams(params);

        }


    }

}
