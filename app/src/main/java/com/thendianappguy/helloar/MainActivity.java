package com.thendianappguy.helloar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    /** Creating an ArFragment */
    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** taking reference to it with id from XML */
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        /** when even ever user taps on the plane detected by the ARFragment this will be called */
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            /** An anchor describes a fixed location and orientation in the real world
             * used to render 3d model on the top of it */
            Anchor anchor = hitResult.createAnchor();

            /** Creating the 3d model */
            ModelRenderable.builder().setSource(this, Uri.parse("model.sfb"))
                    .build()
                    /** Adding model to the scene */
                    .thenAccept(modelRenderable -> addModelToScene(anchor,modelRenderable))
                    .exceptionally(throwable -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(throwable.getMessage()).show();
                        return null;
                    });
        });
    }

    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {

        /** Creating an anchor node based on the anchor which has been creating due to tap */
        AnchorNode anchorNode = new AnchorNode(anchor);

        /** We cannot move the anchor node or increase its size or decrease, to achive increase
         * in size and decrease in size we are using transformablenode
         */
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());

        /** positionning the transofrmable node where anchor node is placed */
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }
}
