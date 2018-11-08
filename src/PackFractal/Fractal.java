package PackFractal;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Fractal {
    private int tamanho;
    private int interacoes;
    private FormaGeometricaEnum forma;

    Fractal(int tamanho, int interacoes, FormaGeometricaEnum forma) {
        this.tamanho = tamanho;
        this.interacoes = interacoes;
        this.forma = forma;
    }

    public static void GeraArquivoSVG(Fractal f) throws Exception {
        FileWriter saida = new FileWriter("saida_fractal.svg");
        PrintWriter gravarSaida = new PrintWriter(saida);
        gravarSaida.printf("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + (f.tamanho * f.interacoes * 2) +
                "\" height=\"" + (f.tamanho * f.interacoes * 2) + "\" version=\"1.1\">");

        String geometrico = "\n\t", posxInicial, posyInicial, tamanho,
                desenho = " stroke=\"black\" stroke-width=\"1\" fill=\"black\"/>";
        int px, py;
        if (f.forma == FormaGeometricaEnum.CIRCULO) {
            geometrico += "<circle";
            px = 1 + f.tamanho / 2;
            py = 1 + f.tamanho / 2;
            posxInicial = " cx=\"" + px + "\"";
            posyInicial = " cy=\"" + py + "\"";
            tamanho = " r=\"" + (f.tamanho / 2) +"\"";
        }
        else {
            geometrico += "<rect";
            px = 1;
            py = 1;
            posxInicial = " x=\"" + px + "\"";
            posyInicial = " y=\"" + py + "\"";
            tamanho = " width=\"" + f.tamanho +"\"" + " height=\"" + f.tamanho +"\"";
        }
        gravarSaida.printf(geometrico + posxInicial + posyInicial + tamanho + desenho);


        String posx, posy;
        int camadas = 1;
        while (camadas < f.interacoes){
            if (camadas % 2 == 0)
                desenho = " stroke=\"black\" stroke-width=\"0\" fill=\"black\"/>";
            else
                desenho = " stroke=\"blue\" stroke-width=\"2\" fill=\"white\"/>";
            posx = GeraPontas(px, f.tamanho * camadas, f.forma, true);
            gravarSaida.printf(geometrico + posx + posyInicial + tamanho + desenho);
            posy = GeraPontas(py, f.tamanho * camadas, f.forma, false);
            gravarSaida.printf(geometrico + posxInicial + posy + tamanho + desenho);
            posx = GeraCentro(px, f.tamanho * camadas, f.forma, true);
            posy = GeraCentro(py, f.tamanho * camadas, f.forma, false);
            gravarSaida.printf(geometrico + posx + posy + tamanho + desenho);
            camadas++;
        }
        if(f.forma.is(FormaGeometricaEnum.QUADRADO) && camadas > 3) {

            for (int camada = 4; camada <= camadas; camada++ ){
                if (camada % 2 == 0)
                    desenho = " stroke=\"black\" stroke-width=\"0\" fill=\"black\"/>";
                else
                    desenho = " stroke=\"blue\" stroke-width=\"2\" fill=\"white\"/>";
                String posicao = GeraInterior(f.tamanho, camada);
                gravarSaida.printf(geometrico + posicao + tamanho + desenho);
            }
        }
        gravarSaida.printf("\n</svg>");
        gravarSaida.close();
        saida.close();
        System.out.println("Arquivo Gerado.");
    }

    private static String GeraCentro(int posicao, int tamanho, FormaGeometricaEnum forma, boolean coordenada){
        if(forma.is(FormaGeometricaEnum.CIRCULO))
            return coordenada ? " cx=\"" + (posicao + tamanho) + "\"" : " cy=\"" + (posicao + tamanho) + "\"";
        else
            return coordenada ? " x=\"" + (posicao + tamanho) + "\"" : " y=\"" + (posicao + tamanho) + "\"";
    }

    private static String GeraPontas(int posicao, int tamanho, FormaGeometricaEnum forma, boolean coordenada){
        if(forma.is(FormaGeometricaEnum.CIRCULO))
            return coordenada ? " cx=\"" + (posicao + tamanho * 1.5) + "\"" : " cy=\"" + (posicao + tamanho * 1.5) + "\"";
        else
            return coordenada ? " x=\"" + (posicao + tamanho * 2) + "\"" : " y=\"" + (posicao + tamanho * 2) + "\"";
    }

    private static String GeraInterior(int tamanho, int camada){

        return " x=\"" + tamanho + "\" y=\"" + camada + "\"";
    }
}