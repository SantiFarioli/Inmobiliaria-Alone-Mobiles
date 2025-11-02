package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.santisoft.inmobiliariaalone.databinding.FragmentAgregarInmuebleBinding;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.util.DialogUtils;
import com.santisoft.inmobiliariaalone.util.DialogEvent;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AgregarInmuebleFragment extends Fragment {

    private FragmentAgregarInmuebleBinding binding;
    private AgregarInmuebleViewModel vm;
    private ActivityResultLauncher<Intent> launcherGaleria;
    private SweetAlertDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgregarInmuebleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(AgregarInmuebleViewModel.class);
        requireActivity().setTitle("Crear Inmueble"); // ✅ título correcto
        configurarSpinners();
        configurarLauncher();
        observarViewModel();
        return binding.getRoot();
    }

    private void observarViewModel() {
        vm.getUriFoto().observe(getViewLifecycleOwner(), uri -> binding.ivPreview.setImageURI(uri));

        vm.getDialogEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;
            switch (event.getType()) {
                case LOADING:
                    loadingDialog = DialogUtils.showLoading(requireContext(), event.getTitle());
                    break;
                case HIDE_LOADING:
                    if (loadingDialog != null) loadingDialog.dismissWithAnimation();
                    break;
                case SUCCESS:
                    DialogUtils.showSuccess(requireContext(), event.getTitle(), event.getMessage());
                    if (loadingDialog != null) loadingDialog.dismissWithAnimation();
                    limpiarCampos();
                    requireActivity().getSupportFragmentManager().popBackStack();
                    break;
                case ERROR:
                    DialogUtils.showError(requireContext(), event.getTitle(), event.getMessage());
                    break;
                case WARNING:
                    DialogUtils.showWarning(requireContext(), event.getTitle(), event.getMessage());
                    break;
                case CONFIRM:
                    new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(event.getTitle())
                            .setContentText(event.getMessage())
                            .setConfirmText("Sí, publicar")
                            .setCancelText("Cancelar")
                            .setConfirmClickListener(sDialog -> {
                                sDialog.dismissWithAnimation();
                                Inmueble i = new Inmueble(
                                        0,
                                        binding.etDireccion.getText().toString(),
                                        binding.spUso.getSelectedItem().toString(),
                                        binding.spTipo.getSelectedItem().toString(),
                                        Integer.parseInt(binding.etAmbientes.getText().toString()),
                                        Double.parseDouble(binding.etPrecio.getText().toString()),
                                        binding.cbDisponible.isChecked() ? "Disponible" : "No disponible",
                                        0, null, null, ""
                                );
                                vm.enviarInmueble(i);
                            })
                            .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                            .show();
                    break;
            }
        });
    }

    private void configurarSpinners() {
        ArrayAdapter<String> adapterUso = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, new String[]{"Residencial", "Comercial"});
        adapterUso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spUso.setAdapter(adapterUso);

        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, new String[]{"Casa", "Departamento", "Local"});
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spTipo.setAdapter(adapterTipo);
    }

    private void configurarLauncher() {
        launcherGaleria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                (ActivityResult result) -> vm.recibirFoto(result));
        binding.btnElegirFoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            launcherGaleria.launch(i);
        });
        binding.btnGuardarInmueble.setOnClickListener(v -> vm.guardarInmueble(
                binding.etDireccion.getText().toString(),
                binding.etPrecio.getText().toString(),
                binding.spUso.getSelectedItem().toString(),
                binding.spTipo.getSelectedItem().toString(),
                binding.etAmbientes.getText().toString(),
                binding.cbDisponible.isChecked()
        ));
    }

    private void limpiarCampos() {
        binding.etDireccion.setText("");
        binding.etPrecio.setText("");
        binding.etAmbientes.setText("");
        binding.ivPreview.setImageDrawable(null);
        binding.cbDisponible.setChecked(true);
    }
}
