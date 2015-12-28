package lamp.server.aladin.utils.assembler;

import java.util.List;

public interface ListAssembler<F, T> extends Assembler<F, T> {

	List<T> assemble(List<F> f);

}
