package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;

import org.apache.http.util.TextUtils;

import java.awt.Color;

import http.TranslateHttp;
import moudle.TranslateBean;
import util.Logger;

/**
 * Created by Ian.Lu on 2018/4/3.
 * Project : TranslatePlugin
 */
public class TranslateAction extends AnAction {

    TranslateHttp http = new TranslateHttp();

    @Override
    public void actionPerformed(AnActionEvent e) {
        Logger.init("translate", Logger.DEBUG);

        //获取编辑器
        Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (mEditor == null) {
            Logger.info("编辑器获取失败");
            return;
        }

        //获取选择器：描述光标选中的文本段
        SelectionModel model = mEditor.getSelectionModel();
        String selectText = model.getSelectedText();
        if (TextUtils.isEmpty(selectText)) {
            Logger.info("未选中文字");
            return;
        }

        http.translate(selectText, new TranslateHttp.CallBack() {
            @Override
            public void callback(TranslateBean translateBean) {
                StringBuffer stringBuffer = new StringBuffer();
                String translateResult = translateBean.getTranslation().toString() + "\n";
                stringBuffer.append(translateResult);
                TranslateBean.BasicBean basicBean = translateBean.getBasic();
                if (basicBean != null) {
                    //其他词义
                    String otherTip = "其他词义\n";
                    String other = basicBean.getExplains().toString();

                    stringBuffer.append(otherTip);
                    stringBuffer.append(other);
                }

                showPopupWindow(mEditor, stringBuffer.toString());
            }
        });
    }

    /**
     * 弹窗
     */
    private void showPopupWindow(final Editor editor, final String result) {
        //指定API，按需了解
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory
                        .createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }
}
