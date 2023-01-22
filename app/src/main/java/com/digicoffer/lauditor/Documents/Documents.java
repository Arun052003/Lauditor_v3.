package com.digicoffer.lauditor.Documents;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digicoffer.lauditor.Documents.DocumentsListAdpater.DocumentsListAdapter;
import com.digicoffer.lauditor.Documents.models.ClientsModel;
import com.digicoffer.lauditor.Documents.models.DocumentsModel;
import com.digicoffer.lauditor.Documents.models.MattersModel;
import com.digicoffer.lauditor.R;
import com.digicoffer.lauditor.Webservice.AsyncTaskCompleteListener;
import com.digicoffer.lauditor.Webservice.HttpResultDo;
import com.digicoffer.lauditor.Webservice.WebServiceHelper;
import com.digicoffer.lauditor.common.AndroidUtils;
import com.digicoffer.lauditor.common_adapters.CommonSpinnerAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Documents extends Fragment implements BottomSheetUploadFile.OnPhotoSelectedListner, AsyncTaskCompleteListener {
    Button btn_browse;
    BottomSheetUploadFile bottommSheetUploadDocument;
    private Bitmap mSelectedBitmap;
    private ImageView imageView;
    private File mSelectedUri;
    LinearLayout ll_documents;
    String filename;
    RecyclerView rv_documents;
    ArrayList<DocumentsModel> docsList = new ArrayList<>();
    AlertDialog progress_dialog;
    TextView tv_add_tag;
    Button btn_upload;
    //    AutoCompleteTextView ;
    File file;
    String value = "";
    String entity_id = "";
    String matter_id = "";
    String client_id = "";
    ArrayList<ClientsModel> clientsList = new ArrayList<>();
    Spinner sp_matter, sp_client;
    ArrayList<MattersModel> matterlist = new ArrayList<>();
    ArrayList<ClientsModel> updatedClients = new ArrayList<>();
    TextInputEditText tv_selected_file;
    TextInputLayout tl_selected_file;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.upload_document, container, false);
        btn_browse = v.findViewById(R.id.btn_browse);
        tv_selected_file = v.findViewById(R.id.tv_selected_file);
        tl_selected_file = v.findViewById(R.id.tl_selected_file);
        sp_client = v.findViewById(R.id.at_search_client);
        sp_matter = v.findViewById(R.id.sp_matter);
        tv_add_tag = v.findViewById(R.id.tv_add_tag);
        btn_upload = v.findViewById(R.id.btn_upload);
        rv_documents = v.findViewById(R.id.rv_documents);
        tv_add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUploadDocumentWebservice();
            }
        });
        callClientWebservice();
        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionREAD_EXTERNAL_STORAGE(getContext());
            }
        });
        tl_selected_file.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    tl_selected_file.setHint("");
                else
                    tl_selected_file.setHint("Select Document");
            }
        });
        tv_selected_file.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    tv_selected_file.setHint("");
                else
                    tv_selected_file.setHint("Select Document");
            }
        });
        return v;

    }

    private void callUploadDocumentWebservice() {
        try {
            for (int i = 0; i < docsList.size(); i++) {
                progress_dialog = AndroidUtils.get_progress(getActivity());
                JSONObject jsonObject = new JSONObject();
                JSONArray clients = new JSONArray();
                JSONObject clients_jobject = new JSONObject();
                JSONArray matter = new JSONArray();
                JSONArray tags = new JSONArray();
                String docname = "";
                DocumentsModel documentsModel = docsList.get(i);
                filename = documentsModel.getName();
                File new_file = documentsModel.getFile();
                String doc_type = "pdf";
                String content_string = new_file.getName().replace(".", "/");
                String[] content_type = content_string.split("/");
                if (content_type.length >= 2) {
                    doc_type = content_type[1];
                    docname = content_type[0];
                }

                for (int j = 0; j < clientsList.size(); j++) {
                    if (clientsList.get(j).getId().matches(client_id)) {
                        ClientsModel clientsModel = clientsList.get(j);
                        clients_jobject.put("id", clientsModel.getId());
                        clients_jobject.put("type", clientsModel.getType());
                        clients.put(clients_jobject);
                    }
                }
                matter.put(matter_id);
                jsonObject.put("name", docname);
                jsonObject.put("description", docname);
                jsonObject.put("filename", docname);
                jsonObject.put("category", "client");
                jsonObject.put("clients", clients);
                jsonObject.put("matters", matter);
                jsonObject.put("downloadDisabled", false);
                jsonObject.put("tags", tags);
                if (doc_type.equalsIgnoreCase("apng") || doc_type.equalsIgnoreCase("avif") || doc_type.equalsIgnoreCase("gif") || doc_type.equalsIgnoreCase("jpeg") || doc_type.equalsIgnoreCase("png") || doc_type.equalsIgnoreCase("svg") || doc_type.equalsIgnoreCase("webp") || doc_type.equalsIgnoreCase("jpg")) {
                    jsonObject.put("content_type", "image/" + doc_type);
                } else {
                    jsonObject.put("content_type", "application/" + doc_type);
                }

//            AndroidUtils.showAlert(jsonObject.toString(),getContext());
                WebServiceHelper.callHttpUploadWebService(this, getContext(), WebServiceHelper.RestMethodType.POST, "v3/document/upload", "Upload Document", new_file, jsonObject.toString());

            }
        } catch(Exception e){
                if (progress_dialog != null && progress_dialog.isShowing()) {
                    AndroidUtils.dismiss_dialog(progress_dialog);
                }
                e.printStackTrace();
            }


    }

    private void callClientWebservice() {
        try {
            progress_dialog = AndroidUtils.get_progress(getActivity());
            JSONObject jsonObject = new JSONObject();
            WebServiceHelper.callHttpWebService(this, getContext(), WebServiceHelper.RestMethodType.GET, "v2/relationship/client/list", "Clients List", jsonObject.toString());
        } catch (Exception e) {
            if (progress_dialog != null && progress_dialog.isShowing()) {
                AndroidUtils.dismiss_dialog(progress_dialog);
            }
        }
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AndroidUtils.showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    123);
                }
                return false;
            } else {
                BottomSheetUploadfile();
                return true;
            }

        } else {
            return true;
        }
    }

    //    public void showDialog(final String msg, final Context context,
//                           final String permission) {
//        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//        alertBuilder.setCancelable(true);
//        alertBuilder.setTitle("Permission necessary");
//        alertBuilder.setMessage(msg + " permission is necessary");
//        alertBuilder.setPositiveButton(android.R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions((Activity) context,
//                                new String[]{permission},
//                                123);
//                    }
//                });
//        AlertDialog alert = alertBuilder.create();
//        alert.show();
//    }
    private void BottomSheetUploadfile() {
        bottommSheetUploadDocument = new BottomSheetUploadFile();
        bottommSheetUploadDocument.show(getParentFragmentManager(), "");
        bottommSheetUploadDocument.setTargetFragment(Documents.this, 1);
    }

    @SuppressLint("Range")
    @Override
    public void getImagepath(File imagepath, Uri ImageURI) throws IOException {
        if ((imagepath == null)) {
            mSelectedBitmap = null;
            mSelectedUri = imagepath;
            String uri = imagepath.toString();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            imageLoader.displayImage(String.valueOf(Uri.fromFile(new File(uri))), imageView);
            file = imagepath;
            Cursor c = getContext().getContentResolver().query(ImageURI, null, null, null, null);
            c.moveToFirst();
            String[] content_type = file.getName().split(".");
            String file_name = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            tv_selected_file.setText(file_name);

            load_documents(docsList, file_name, file);
        } else {
            file = getFile(getContext(), ImageURI);
            Log.i("FILE", "Info:" + file.toString());
            String file_name = file.getName();
            tv_selected_file.setText(file_name);
//            DocumentsModel documentsModel = new DocumentsModel();
//            documentsModel.setName(file.getName());
//            docsList.add(documentsModel);
            load_documents(docsList, file_name, file);
//            docsList.add()
        }

    }

    private void load_documents(ArrayList<DocumentsModel> docsList, String file_name, File file) {
//        for (int i = 0; i < docsList.size(); i++) {
//            View view = LayoutInflater.from(getContext()).inflate(R.layout.displays_documents_list, null);
//            TextView tv_docname = view.findViewById(R.id.tv_document_name);
//            tv_docname.setText(docsList.get(i).getName());
        DocumentsModel documentsModel = new DocumentsModel();
        documentsModel.setName(file_name);
        documentsModel.setFile(file);
        docsList.add(documentsModel);

        rv_documents.setLayoutManager(new GridLayoutManager(getContext(), 1));
        DocumentsListAdapter adapter = new DocumentsListAdapter(docsList);
        rv_documents.setAdapter(adapter);
        rv_documents.setHasFixedSize(true);
//            ll_documents.addView(view);
//        }
    }

    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        mSelectedBitmap = bitmap;
        mSelectedUri = null;
//        tv_upload_file.setEnabled(false);
        File filesDir = getContext().getFilesDir();
        File imageFile = new File(filesDir, "bitmap" + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            file = imageFile;
            tv_selected_file.setText(file.getName());
            DocumentsModel documentsModel = new DocumentsModel();
            documentsModel.setName(file.getName());
            docsList.add(documentsModel);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            file = new File(picturePath);
            filename = file.getName();
            tv_selected_file.setText(file.getName());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
//                    upload_file();
                } else {
                    AndroidUtils.showToast("GET_ACCOUNTS Denied", getContext());
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onAsyncTaskComplete(HttpResultDo httpResult) {
        if (progress_dialog != null && progress_dialog.isShowing())
            AndroidUtils.dismiss_dialog(progress_dialog);
        if (httpResult.getResult() == WebServiceHelper.ServiceCallStatus.Success) {
            try {
                JSONObject result = new JSONObject(httpResult.getResponseContent());
                if (httpResult.getRequestType().equals("Clients List")) {
                    JSONObject data = result.getJSONObject("data");
                    try {
                        loadClients(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (httpResult.getRequestType().equals("Legal Matter")) {
                    JSONArray matters = result.getJSONArray("matterList");
                    loadMatters(matters);
                } else if (httpResult.getRequestType().equals("Upload Document")) {
                    String msg = result.getString("msg");
                    AndroidUtils.showToast(msg, getContext());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadMatters(JSONArray matters) throws JSONException {
        for (int i = 0; i < matters.length(); i++) {
            JSONObject jsonObject = matters.getJSONObject(i);
            MattersModel mattersModel = new MattersModel();
            mattersModel.setId(jsonObject.getString("id"));
            mattersModel.setTitle(jsonObject.getString("title"));
            mattersModel.setType(jsonObject.getString("type"));
            matterlist.add(mattersModel);
        }
        initMatter();
    }

    private void initMatter() {
        CommonSpinnerAdapter adapter = new CommonSpinnerAdapter(getActivity(), matterlist);
        Log.i("ArrayList", "Info:" + matterlist);
//        ArrayAdapter adaptador = new ArrayAdapter(User_Profile.this, android.R.layout.simple_spinner_item, sorted_countriesList);
//        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        spinner.setAdapter(adaptador);
        sp_matter.setAdapter(adapter);
        sp_matter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                matter_id = matterlist.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initUI(ArrayList<ClientsModel> clientsList) {
        CommonSpinnerAdapter adapter = new CommonSpinnerAdapter(getActivity(), this.clientsList);
//        Log.i("ArrayList","Info:"+matterlist);
//        ArrayAdapter adaptador = new ArrayAdapter(User_Profile.this, android.R.layout.simple_spinner_item, sorted_countriesList);
//        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        spinner.setAdapter(adaptador);
        sp_client.setAdapter(adapter);
        sp_client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                matter_name = Documents.this.clientsList.get(position).getName();
                client_id = clientsList.get(position).getId();
                matterlist.clear();
                callLegalMatter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadClients(JSONObject data) throws JSONException {
        JSONArray relationships = data.getJSONArray("relationships");

        for (int i = 0; i < relationships.length(); i++) {
            JSONObject jsonObject = relationships.getJSONObject(i);
            ClientsModel clientsModel = new ClientsModel();
            clientsModel.setId(jsonObject.getString("id"));
            clientsModel.setName(jsonObject.getString("name"));
            clientsModel.setType(jsonObject.getString("type"));
            clientsList.add(clientsModel);
//                    updatedClients.add(clientsModel);
        }
        initUI(clientsList);
    }

    private void callLegalMatter() {
        try {
            progress_dialog = AndroidUtils.get_progress(getActivity());
            JSONObject jsonObject = new JSONObject();
            WebServiceHelper.callHttpWebService(this, getContext(), WebServiceHelper.RestMethodType.GET, "v3/matter/all/" + client_id, "Legal Matter", jsonObject.toString());

        } catch (Exception e) {
            if (progress_dialog != null && progress_dialog.isShowing()) {
                AndroidUtils.dismiss_dialog(progress_dialog);
            }
            e.printStackTrace();
        }
    }


}


