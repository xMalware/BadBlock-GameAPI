package fr.badblock.minecraftserver;
import java.io.FilePermission;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyPermission;
import net.minecraft.server.v1_8_R3.MinecraftServer;
public class BadblockSecurityManager extends SecurityManager {
	private static List<ClassLoader> loaders = new ArrayList<ClassLoader>();
	private static List<ClassLoader> allowed = new ArrayList<ClassLoader>();
	
	static
	{
		allowed.add(BadblockSecurityManager.class.getClassLoader());
	}
	
	public static void addAllowedLoader(ClassLoader loader)
	{
		System.getSecurityManager().checkPermission(new RuntimePermission("addLoaderInLists"));
		allowed.add(loader);
	}
	
	public static void addDisallowedLoader(ClassLoader loader)
	{
		System.getSecurityManager().checkPermission(new RuntimePermission("addLoaderInLists"));
		loaders.add(loader);
	}
	
	private boolean isConcerned()
	{
		Class<?>[] context = getClassContext();
		
		for(int i = 2; i < context.length; i++)
			if(loaders.contains( context[i].getClassLoader() ))
				return true;
			else if(allowed.contains( context[i].getClassLoader() ) && i > 2)
				return false;
		return false;
	}
	
	private void checkFileAccess(String file)
	{
		Path opening = Paths.get(file).toAbsolutePath();
		Path limited = Paths.get("plugins/apiPlugins").toAbsolutePath();
		
		if(!opening.startsWith(limited))
			throw new SecurityException("File access violation: " + file);		
	}
	
	@Override
	public void checkConnect(String host, int port, Object context) {
		checkConnect(host, port);
	}
	
	@Override
	public void checkConnect(String host, int port) {
		if(isConcerned())
			throw new SecurityException();
	}
	
	@Override
	public void checkAccess(Thread t) {
		if(isConcerned() && MinecraftServer.getServer().primaryThread.equals(t))
			throw new SecurityException();
	}
	
	@Override
	public void checkExit(int status) {
		if(isConcerned())
			throw new SecurityException();
	}
	
	@Override
	public void checkExec(String cmd) {
		if(isConcerned())
			throw new SecurityException();
	}
	
	@Override
	public void checkLink(String link) {
		if(isConcerned())
			throw new SecurityException();
	}
	
	@Override
	public void checkAccept(String host, int port) {
		if(isConcerned())
			throw new SecurityException();
	}
	
	@Override
	public void checkListen(int port) {
		if(isConcerned())
			throw new SecurityException();
	}
	
	@Override
	public void checkPrintJobAccess() {
		if(isConcerned())
			throw new SecurityException();
	}
	
	@Override
	public void checkRead(String file) {
		if(isConcerned())
			checkFileAccess(file);
	}
	
	@Override
	public void checkWrite(String file) {
		if(isConcerned())
			checkFileAccess(file);
	}
	
	@Override
	public void checkDelete(String file) {
		if(isConcerned())
			checkFileAccess(file);
	}
	
	@Override
	public void checkSecurityAccess(String target) {
		if(isConcerned())
			throw new SecurityException();
	}
	
	@Override
    public void checkPermission(Permission perm, Object context)
    {
		checkPermission(perm);
    }
	
	@Override
    public void checkPermission(Permission perm)
    {
		if(!isConcerned())
			return;
		
		if(perm instanceof FilePermission)
			checkFileAccess(perm.getName());
		else if(perm instanceof RuntimePermission)
		{
			switch(perm.getName())
			{
				case "getenv":
				case "modifyThread":
				case "stopThread":
				case "accessDeclaredMembers":
					return;
				default:
					System.out.println(perm.getName());
					throw new SecurityException(perm.getName());
					
			
			}
			
		}
		else if(perm instanceof PropertyPermission)
		{
			if(perm.getActions().contains("write"))
				throw new SecurityException();
		}
    }
}