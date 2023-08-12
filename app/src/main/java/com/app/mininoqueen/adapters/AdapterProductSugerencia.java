package com.app.mininoqueen.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mininoqueen.R;
import com.app.mininoqueen.modelos.Product;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterProductSugerencia extends RecyclerView.Adapter<AdapterProductSugerencia.ViewHolder> implements

        View.OnClickListener {

    private List<Product> courses;
    private LayoutInflater inflater;
    private View.OnClickListener listener;

    private Context context;

    public AdapterProductSugerencia(
            Context context,
            List<Product> courses) {

        this.inflater = LayoutInflater.from(context);
        this.courses = courses;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_products_sugerencia, parent, false);
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
        private final TextView txtVentaStockSug;
        private final TextView txtVentaTotalSug;
        private final EditText txtVentaCantidadkSug;
        private final ImageView imageProduct;

        private Button btnReview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.txtVentaTitleSug);
            textDescription = itemView.findViewById(R.id.txtVentaPrecioSug);
            txtVentaStockSug = itemView.findViewById(R.id.txtVentaStockSug);
            txtVentaTotalSug = itemView.findViewById(R.id.txtVentaTotalSug);
            txtVentaCantidadkSug = itemView.findViewById(R.id.txtVentCantidadSug);
            imageProduct = itemView.findViewById(R.id.imageVentaProSug);
            btnReview = itemView.findViewById(R.id.btnVentaAddSug);

            txtVentaCantidadkSug.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    txtVentaTotalSug.setText("0");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!txtVentaCantidadkSug.getText().toString().isEmpty()) {
                        int cantidad = Integer.parseInt(txtVentaCantidadkSug.getText().toString());

                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Product planification = courses.get(position);
                            Double total = cantidad * planification.getPrecioVenta();
                            txtVentaTotalSug.setText("" + total);
                        }
                    } else {
                        txtVentaTotalSug.setText("0.0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            // here the listener of the button that is in the list item is configured
            btnReview.setOnClickListener(v -> {
                if (buttonClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Product planification = courses.get(position);
                        Map<String, Object> data = new HashMap<>();
                        data.put("cantidad", txtVentaCantidadkSug.getText().toString());
                        data.put("total", txtVentaTotalSug.getText().toString());

                        // aqui validamos que no este vacio y que no sobre pase el stock
                        buttonClickListener.onButtonClicked(planification, data, v);

                        if (!txtVentaCantidadkSug.getText().toString().isBlank()) {

                            int cantidad = Integer.parseInt(txtVentaCantidadkSug.getText().toString());

                            if (cantidad <= planification.getStock()) {
                                planification.setStock(planification.getStock() - Integer.parseInt(txtVentaCantidadkSug.getText().toString()));
                                txtVentaStockSug.setText("" + planification.getStock());
                                txtVentaTotalSug.setText("0.0");
                            }


                        }


                    }
                }
            });
        }

        public void setData(Product course) {

            Glide.with(context)
                    .load(course.getImagen())
                    .into(imageProduct);
            textTitle.setText(course.getDescripcion());
            textDescription.setText("" + course.getPrecioVenta());
            txtVentaStockSug.setText("" + course.getStock());

        }


    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClicked(Product product, Map<String, Object> data, View view);
    }


    private OnButtonClickListener buttonClickListener;

}
