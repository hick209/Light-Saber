package tang.helper.gl;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.Display;

import tang.helper.utils.Console;

//TODO your shaders are an abomination, michael. fix them.
public class GLShader {

	int shaderProgram = 0;
	public GLShader(String vertShaderSrc, String fragShaderSrc) {
		shaderProgram = glCreateProgram();
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        StringBuilder vertexShaderSource = new StringBuilder();
        StringBuilder fragmentShaderSource = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(vertShaderSrc));
            String line;
            while ((line = reader.readLine()) != null) {
                vertexShaderSource.append(line).append('\n');
            }
        } catch (IOException e) {
            Console.error("Vertex shader wasn't loaded properly.");
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        BufferedReader reader2 = null;
        try {
            reader2 = new BufferedReader(new FileReader(fragShaderSrc));
            String line;
            while ((line = reader2.readLine()) != null) {
                fragmentShaderSource.append(line).append('\n');
            }
        } catch (IOException e) {
        	Console.error("Fragment shader wasn't loaded properly.");
            Display.destroy();
            System.exit(1);
        } finally {
            if (reader2 != null) {
                try {
                    reader2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
        	Console.error("Vertex shader wasn't able to be compiled correctly.\n" + glGetShaderInfoLog(vertexShader, 1024));
        }
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
        	Console.error("Fragment shader wasn't able to be compiled correctly.\n" + glGetShaderInfoLog(fragmentShader, 1024));
        }
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
        	Console.error("Shader program wasn't linked correctly.\n" + glGetProgramInfoLog(shaderProgram, 1024));
        }
        glValidateProgram(shaderProgram);
        
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
	}
	
	public void use() {
        glUseProgram(shaderProgram);
	}
	
	public void release() {
        glUseProgram(0);
	}
}
