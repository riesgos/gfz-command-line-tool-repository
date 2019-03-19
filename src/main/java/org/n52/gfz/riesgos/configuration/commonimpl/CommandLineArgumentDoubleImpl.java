package org.n52.gfz.riesgos.configuration.commonimpl;

import org.n52.gfz.riesgos.commandlineparametertransformer.LiteralDoubleBindingToStringCmd;
import org.n52.gfz.riesgos.configuration.impl.IdentifierWithBindingImpl;
import org.n52.wps.io.data.binding.literal.LiteralDoubleBinding;

import java.util.Optional;

public class CommandLineArgumentDoubleImpl extends IdentifierWithBindingImpl {
    public CommandLineArgumentDoubleImpl(final String identifier) {
        super(
                identifier,
                LiteralDoubleBinding.class,
                Optional.empty(),
                Optional.of(new LiteralDoubleBindingToStringCmd()),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }
}
