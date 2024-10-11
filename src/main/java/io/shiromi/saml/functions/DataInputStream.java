package io.shiromi.saml.functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface DataInputStream {
    char[] read(File f) throws IOException;
}
