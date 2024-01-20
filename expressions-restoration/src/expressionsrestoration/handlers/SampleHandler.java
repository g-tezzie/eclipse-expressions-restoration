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
		Stream<String> map = getExpressionsInView();

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		String filePath = showFileDialog(window, false);

		Path path = Path.of(filePath);
		boolean loadNorSave = Files.isReadable(path);

		if (loadNorSave)
			try {
				Stream<String> map2 = loadFromFile(filePath);
				setExpressionsInView(map2);
			} catch (IOException | ClassNotFoundException | ClassCastException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MessageDialog.openInformation(window.getShell(), "Expressions-restoration", "Load failed: " + filePath);
				return null;
			}
		else
			try {
				saveToFile(filePath, map);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				MessageDialog.openInformation(window.getShell(), "Expressions-restoration", "Save failed: " + filePath);
				return null;
			}

		MessageDialog.openInformation(window.getShell(), "Expressions-restoration",
				"Expressions are " + (loadNorSave ? "loaded from" : "saved to") + ": " + filePath);
		return null;
	}

	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Stream<String> loadFromFile(String filePath) throws IOException, ClassNotFoundException {
		Path path = Path.of(filePath);
		InputStream newInputStream = Files.newInputStream(path, StandardOpenOption.READ);
		Stream<String> map2 = extracted(newInputStream);
		return map2;
	}

	/**
	 * @param open
	 * @param map
	 * @return
	 * @throws IOException
	 */
	private void saveToFile(String open, Stream<String> map) throws IOException {
		Path path = Path.of(open);
		OpenOption options = StandardOpenOption.CREATE_NEW;
		OutputStream newOutputStream = Files.newOutputStream(path, options);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(newOutputStream);
		Object[] array = map.toArray();
		List<Object> from = List.of(array);
		objectOutputStream.writeObject(from);
		newOutputStream.close();
	}

	/**
	 * @param map2
	 */
	private void setExpressionsInView(Stream<String> map2) {
		IExpressionManager expressionManager1 = DebugPlugin.getDefault().getExpressionManager();
		map2.forEach(x -> expressionManager1.addExpression(expressionManager1.newWatchExpression(x)));
	}

	/**
	 * @return
	 */
	private Stream<String> getExpressionsInView() {
		IExpressionManager expressionManager = DebugPlugin.getDefault().getExpressionManager();
		IExpression[] expressions = expressionManager.getExpressions();
		Stream<String> map = Arrays.stream(expressions).map(r -> r.getExpressionText());
		return map;
	}

	/**
	 * @param window
	 * @return null by cancelled, or file path
	 */
	private String showFileDialog(IWorkbenchWindow window, boolean warnOverwrite) {
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
		dialog.setOverwrite(warnOverwrite);
		String open = dialog.open();
		return open;
	}

	public Stream<String> extracted(InputStream newInputStream)
			throws IOException, ClassNotFoundException, ClassCastException {
		ObjectInputStream objectInputStream = new ObjectInputStream(newInputStream);
		Object readObject = objectInputStream.readObject();
		Stream<String> map2 = List.copyOf((Collection<?>) readObject).stream().map((x) -> (String) x);
		return map2;
	}
}
