// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

package cbit.vcell.bionetgen.server;

public final class LocalBNGService_Stub
    extends java.rmi.server.RemoteStub
    implements cbit.vcell.bionetgen.server.BNGService, java.rmi.Remote
{
    private static final long serialVersionUID = 2;
    
    private static java.lang.reflect.Method $method_executeBNG_0;
    
    static {
	try {
	    $method_executeBNG_0 = cbit.vcell.bionetgen.server.BNGService.class.getMethod("executeBNG", new java.lang.Class[] {cbit.vcell.bionetgen.server.BNGInput.class});
	} catch (java.lang.NoSuchMethodException e) {
	    throw new java.lang.NoSuchMethodError(
		"stub class initialization failed");
	}
    }
    
    // constructors
    public LocalBNGService_Stub(java.rmi.server.RemoteRef ref) {
	super(ref);
    }
    
    // methods from remote interfaces
    
    // implementation of executeBNG(BNGInput)
    public cbit.vcell.bionetgen.server.BNGOutput executeBNG(cbit.vcell.bionetgen.server.BNGInput $param_BNGInput_1)
	throws org.vcell.util.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_executeBNG_0, new java.lang.Object[] {$param_BNGInput_1}, 5716613448246541433L);
	    return ((cbit.vcell.bionetgen.server.BNGOutput) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (org.vcell.util.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
}
