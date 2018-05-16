package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;

import java.util.List;

/**
 * 案例库
 */
public class PuttedForwardFragment extends Fragment implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Integer pageNumber = 0;
    private PDFView mPdfView;
    private View mInflate;

    public PuttedForwardFragment() {
        // Required empty public constructor
    }

    public static PuttedForwardFragment newInstance(String param1, String param2) {
        PuttedForwardFragment fragment = new PuttedForwardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mInflate ==null){
            mInflate = inflater.inflate(R.layout.fragment_putted_forward, container, false);

        }
        initView(mInflate);
        return mInflate;
    }


    public void initView(View rootView) {
       final RecyclerView rc = rootView.findViewById(R.id.rc);
        mPdfView = rootView.findViewById(R.id.pdfView);

        mPdfView.fromAsset("case1.pdf")
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(BaseApplication.getApplictaion()))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();



    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
//        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
//        PdfDocument.Meta meta = mPdfView.getDocumentMeta();

        printBookmarksTree(mPdfView.getTableOfContents(), "-");
    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {


            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}
