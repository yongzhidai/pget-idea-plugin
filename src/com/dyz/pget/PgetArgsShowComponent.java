package com.dyz.pget;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.impl.source.PsiJavaCodeReferenceElementImpl;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

/**
 * @author ：daiyongzhi
 * @date ：Created in 20:45 2020/6/16
 * description ：pget框架展示参数组件
 * modified by ：
 */
public class PgetArgsShowComponent implements ApplicationComponent {
    public PgetArgsShowComponent() {
    }

    @Override
    public void initComponent() {

        EditorFactory.getInstance().getEventMulticaster().addEditorMouseListener(new EditorMouseListener() {
            @Override
            public void mousePressed(EditorMouseEvent editorMouseEvent) {

            }

            @Override
            public void mouseClicked(EditorMouseEvent editorMouseEvent) {
                Editor editor = editorMouseEvent.getEditor();
                if(editor == null){
                    return;
                }
                PsiElement psiElement = PsiUtilBase.getElementAtCaret(editor);
                if(psiElement == null){
                    return;
                }
                if(!(psiElement.getParent() instanceof PsiJavaCodeReferenceElementImpl)){
                    return;
                }
                PsiJavaCodeReferenceElementImpl javaCodeReference = (PsiJavaCodeReferenceElementImpl) psiElement.getParent();
                if(!(javaCodeReference.getParent() instanceof PsiTypeElement)){
                    return;
                }
                PsiTypeElement psiTypeElement = (PsiTypeElement) javaCodeReference.getParent();
                PsiClass selectClass = PsiTypesUtil.getPsiClass(psiTypeElement.getType());
                if(selectClass == null){
                    return;
                }
                PgetArgsShowUtil.showArgs(editor.getProject(),selectClass,editor);
            }

            @Override
            public void mouseReleased(EditorMouseEvent editorMouseEvent) {

            }

            @Override
            public void mouseEntered(EditorMouseEvent editorMouseEvent) {

            }

            @Override
            public void mouseExited(EditorMouseEvent editorMouseEvent) {

            }
        });

    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "PgetArgsShowComponent";
    }
}
