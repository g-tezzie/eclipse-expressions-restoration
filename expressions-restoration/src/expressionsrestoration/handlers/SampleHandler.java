package expressionsrestoration.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IExpressionManager;
import org.eclipse.debug.core.model.IExpression;
import org.eclipse.debug.core.model.IWatchExpression;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		{
			Shell shell = window.getShell();
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			String platform = SWT.getPlatform();
			String[] filterNames = new String[] { "Java Binary Object (*.jbo)", "All Files (*)" };
			String[] filterExtensions = new String[] { "*.jbo", "*" };
			String filterPath = platform.equals("win32") ? "c:\\" : "/";
			dialog.setFilterNames(filterNames);
			dialog.setFilterExtensions(filterExtensions);
			dialog.setFilterPath(filterPath);
			dialog.setFileName("expressions");
			String open = dialog.open();
			if (open != null)
				System.out.println("Save to: " + open);
		}

		IExpressionManager expressionManager = DebugPlugin.getDefault().getExpressionManager();
		IWatchExpression newWatchExpression = expressionManager.newWatchExpression("piyo");
		expressionManager.addExpression(newWatchExpression);

		IExpression[] expressions = expressionManager.getExpressions();

		Stream<String> map = Arrays.stream(expressions).map(r -> r.getExpressionText());

		Path path = Path.of("hoge");
		OpenOption options = StandardOpenOption.CREATE_NEW;
		try {
			OutputStream newOutputStream = Files.newOutputStream(path, options);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(newOutputStream);
			Object[] array = map.toArray();
			List<Object> from = List.of(array);
			objectOutputStream.writeObject(from);
			newOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			InputStream newInputStream = Files.newInputStream(path, StandardOpenOption.READ);
			Stream<String> map2 = extracted(newInputStream);
			map2.forEach(x -> expressionManager.addExpression(expressionManager.newWatchExpression(x)));
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MessageDialog.openInformation(window.getShell(), "Expressions-restoration", "Hello, Eclipse world");
		return null;
	}

	public Stream<String> extracted(InputStream newInputStream)
			throws IOException, ClassNotFoundException, ClassCastException {
		ObjectInputStream objectInputStream = new ObjectInputStream(newInputStream);
		Object readObject = objectInputStream.readObject();
		Stream<String> map2 = List.copyOf((Collection<?>) readObject).stream().map((x) -> (String) x);
		return map2;
	}
}
