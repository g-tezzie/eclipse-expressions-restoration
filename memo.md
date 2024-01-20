untitiled.md

回答なし
https://www.eclipse.org/forums/index.php?t=msg&th=1088960

作ろう

https://qiita.com/BooookStore/items/86366582dd72e209a65a

https://qiita.com/ota-meshi/items/b6f776e3b1b7ff8b7f93

https://www.ibm.com/docs/ja/cloud-pak-system-w3500/2.3.1.0?topic=SSZQFR_2.3.1.0/doc/iwd/pgr_plugindeveclipse.htm

https://atmarkit.itmedia.co.jp/fjava/rensai3/kantanplgn01/kantanplgn01_2.html

https://atmarkit.itmedia.co.jp/ait/articles/0509/16/news130.html

Preferences のエクスポートには出てくるが，インポートしても再現されない

```
/instance/org.eclipse.debug.core/prefWatchExpressions=<?xml version\="1.0" encoding\="UTF-8" standalone\="no"?>\r\n<watchExpressions>\r\n    <expression enabled\="true" text\="attributes"/>\r\n    <expression enabled\="true" text\="peripheral"/>\r\n    <expression enabled\="true" text\="registers"/>\r\n    <expression enabled\="true" text\="item.getNodeName()"/>\r\n    <expression enabled\="true" text\="item.getChildNodes().item(2).getNodeName()"/>\r\n    <expression enabled\="true" text\="infoSymbol"/>\r\n    <expression enabled\="true" text\="string"/>\r\n    <expression enabled\="true" text\="infoSymbol.address.toString(16)"/>\r\n    <expression enabled\="true" text\="strDerivedFrom"/>\r\n    <expression enabled\="true" text\="name"/>\r\n    <expression enabled\="true" text\="name \=\= strDerivedFrom"/>\r\n    <expression enabled\="true" text\="name.compareTo(strDerivedFrom)"/>\r\n    <expression enabled\="true" text\="name.contentEquals(strDerivedFrom)"/>\r\n</watchExpressions>\r\n
```

> $ find . -exec grep strDerived {} \+

```
./.plugins/org.eclipse.core.runtime/.settings/org.eclipse.debug.core.prefs:prefWatchExpressions=<?xml version\="1.0" encoding\="UTF-8" standalone\="no"?>\r\n<watchExpressions>\r\n    <expression enabled\="true" text\="peripheral"/>\r\n    <expression enabled\="true" text\="registers"/>\r\n    <expression enabled\="true" text\="item.getNodeName()"/>\r\n    <expression enabled\="true" text\="item.getChildNodes().item(2).getNodeName()"/>\r\n    <expression enabled\="true" text\="infoSymbol"/>\r\n    <expression enabled\="true" text\="string"/>\r\n    <expression enabled\="true" text\="infoSymbol.address.toString(16)"/>\r\n    <expression enabled\="true" text\="strDerivedFrom"/>\r\n    <expression enabled\="true" text\="name"/>\r\n    <expression enabled\="true" text\="name \=\= strDerivedFrom"/>\r\n    <expression enabled\="true" text\="name.compareTo(strDerivedFrom)"/>\r\n    <expression enabled\="true" text\="name.contentEquals(strDerivedFrom)"/>\r\n</watchExpressions>\r\n
```

https://help.eclipse.org/latest/nav/2

Preference サービスを使えればよいが，そうだったらインポートできてもよいはず

``` java
...
IPreferencesService service = Platform.getPreferencesService();
...
```

においを嗅ぎまわって [Interface IExpressionManager](https://help.eclipse.org/latest/rtopic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/debug/core/IExpressionManager.html) まで来た。先の latest なリンクが機能しないなら，[2023-12](https://help.eclipse.org/2023-12/rtopic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/debug/core/IExpressionManager.html) を試してほしい。

``` java
...
IExpressionManager expressionManager = DebugPlugin.getDefault().getExpressionManager();
IWatchExpression newWatchExpression = expressionManager.newWatchExpression("piyo");
expressionManager.addExpression(newWatchExpression);
...
```

## ファイルダイアログ

org.eclipse.jface.dialogs.Dialog がいっぱいあって迷う

https://www.ne.jp/asahi/hishidama/home/tech/eclipse/plugin/develop/dialog.html#h_FileDialog

http://www.eclipse-tips.com/how-to-guides/5-selection-dialogs-in-eclipse

SaveAsDialog がない

```
AbstractElementListSelection Dialog - org.eclipse.ui.dialogs
Checked TreeSelection Dialog - org.eclipse.ui.dialogs
ContainerCheckedTreeViewer - org.eclipse.ui.dialogs
EditorSelection Dialog - org.eclipse.ui.dialogs
ElementListSelectionDialog - org.eclipse.ui.dialogs
ElementTreeSelection Dialog - org.eclipse.ui.dialogs
FileEditor MappingContentProvider - org.eclipse.ui.dialogs
FileEditorMappingLabelProvider - org.eclipse.ui.dialogs
FileSystemElement - org.eclipse.ui.dialogs
FilteredItems SelectionDialog - org.eclipse.ui.dialogs
FilteredList-org.eclipse.ui.dialogs
Filtered Tree-org.eclipse.ui.dialogs
IOverwriteQuery-org.eclipse.ui.dialogs
ISelectionStatusValidator-org.eclipse.ui.dialogs
ISelectionValidator- org.eclipse.ui.dialogs
IStyledStringHighlighter - org.eclipse.ui.dialogs
WorkingSetEditWizard - org.eclipse.ui.dialogs
Working SetNew Wizard - org.eclipse.ui.dialogs
IWorkingSetPage- org.eclipse.ui.dialogs
IWorkingSetSelection Dialog - org.eclipse.ui.dialogs
ListDialog-org.eclipse.ui.dialogs
ListSelection Dialog - org.eclipse.ui.dialogs
PatternFilter-org.eclipse.ui.dialogs
PreferenceLinkArea - org.eclipse.ui.dialogs
PreferencesUtil-org.eclipse.ui.dialogs
Property DialogAction - org.eclipse.ui.dialogs
Property Page - org.eclipse.ui.dialogs Search Pattern - org.eclipse.ui.dialogs
Selection Dialog- org.eclipse.ui.dialogs
Selection Status Dialog - org.eclipse.ui.dialogs
StyledStringHighlighter-org.eclipse.ui.dialogs
TwoPaneElementSelector - org.eclipse.ui.dialogs
TypeFiltering Dialog - org.eclipse.ui.dialogs
WorkingSetConfigurationBlock-org.eclipse.ui.dialogs
WorkingSetGroup - org.eclipse.ui.dialogs
YesNoCancelListSelectionDialog - org.eclipse.ui.dialogs
```

swt の FileDialog つかおう

## パッケージング

https://help.eclipse.org/2022-09/advanced/print.jsp?topic=/org.eclipse.platform.doc.isv/guide/product.htm&cp=2_0_21

https://atmarkit.itmedia.co.jp/ait/articles/0604/20/news141.html
