package com.dyz.pget;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.impl.search.JavaSourceFilterScope;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassesWithAnnotatedMembersSearch;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.util.Processor;
import com.intellij.util.Query;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author ：daiyongzhi
 * @date ：Created in 18:42 2020/6/16
 * description ：
 * modified by ：
 */
public class PgetArgsShowUtil {

    public static void showArgs(Project project,PsiClass selectClass,Editor mEditor){

        PsiType selectClassType = PsiTypesUtil.getClassType(selectClass);
        GlobalSearchScope searchScope = JavaSourceFilterScope.projectScope(project);
        PsiClass bizDataClass = JavaPsiFacade.getInstance(project).findClass("com.dyz.pget.bizdata.IBizData",searchScope);
        if(bizDataClass == null){
            return;
        }
        if(!PsiTypesUtil.getClassType(bizDataClass).isAssignableFrom(selectClassType)){
            return;
        }
        PsiClass providerClass = JavaPsiFacade.getInstance(project).findClass("com.dyz.pget.provider.annotation.BizDataProvider",searchScope);
        if(providerClass == null){
            return;
        }
        Query<PsiClass> query = ClassesWithAnnotatedMembersSearch.search(providerClass,searchScope);

        query.forEachAsync(new Processor<PsiClass>() {
            @Override
            public boolean process(PsiClass psiClass) {
                PsiMethod[] psiMethods = psiClass.getMethods();
                if(psiMethods == null || psiMethods.length <= 0){
                    return true;
                }
                for (PsiMethod method : psiMethods) {
                    PsiType returnType = method.getReturnType();
                    if(returnType == null){
                        continue;
                    }
                    if(selectClassType.isAssignableFrom(returnType)){
                        PsiParameterList psiParameterList = method.getParameterList();
                        PsiParameter[] parameters = psiParameterList.getParameters();
                        StringBuilder sb = new StringBuilder("参数: ( ");
                        if(parameters != null && parameters.length > 0){
                            for(PsiParameter psiParameter : parameters){
                                sb.append(psiParameter.getText()).append(", ");
                            }
                            sb.deleteCharAt(sb.length()-1);
                            sb.deleteCharAt(sb.length()-1);
                        }
                        sb.append(" )");
                        showPopupBalloon(mEditor,sb.toString());
                        return false;
                    }
                }
                return true;
            }
        });
    }

    private static void showPopupBalloon(final Editor editor, final String result) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                JPanel dialogPanel = new JPanel(new BorderLayout());
                dialogPanel.setBorder(new EmptyBorder(5,5,5,5));
                dialogPanel.setOpaque(true);
                dialogPanel.setBackground(new Color(255, 251, 135));
                JLabel label = new JLabel(result);
                dialogPanel.add(label, BorderLayout.CENTER);
                factory.createBalloonBuilder(dialogPanel)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }
}
