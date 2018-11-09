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
        if (f.tamanho < 10 || f.tamanho > 60)
            throw new Exception("Tamanho inválido.");
        if (f.interacoes < 1 || f.interacoes > 15)
            throw new Exception("Número de interações inválido.");

        FileWriter saida = new FileWriter("saida_fractal.svg");
        PrintWriter gravarSaida = new PrintWriter(saida);
        double tamanhoTela = ((f.forma.is(FormaGeometricaEnum.QUADRADO) ? f.tamanho : f.tamanho / 1.3)
                * f.interacoes * 2);
        gravarSaida.printf("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + tamanhoTela +
                "\" height=\"" + tamanhoTela + "\" version=\"1.1\">");

        String geometrico = "\n\t", posxInicial, posyInicial, tamanho,
                desenho = " stroke=\"black\" stroke-width=\"0\" fill=\"blue\"/>";
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
            if(f.forma.is(FormaGeometricaEnum.QUADRADO)) {
                posx = GeraPontasQuadrado(px, f.tamanho * camadas, true);
                gravarSaida.printf(geometrico + posx + posyInicial + tamanho + desenho);
                posy = GeraPontasQuadrado(py, f.tamanho * camadas, false);
                gravarSaida.printf(geometrico + posxInicial + posy + tamanho + desenho);
            }
            else {
                posx = GeraPontasCirculo(px, f.tamanho * camadas, camadas, f.interacoes, true);
                gravarSaida.printf(geometrico + posx + tamanho + desenho);
                posy = GeraPontasCirculo(px, f.tamanho * camadas, camadas, f.interacoes, false);
                gravarSaida.printf(geometrico + posy + tamanho + desenho);
            }
            posx = GeraCentro(px, f.tamanho * camadas, f.forma, true);
            posy = GeraCentro(py, f.tamanho * camadas, f.forma, false);
            gravarSaida.printf(geometrico + posx + posy + tamanho + desenho);
            camadas++;
        }
        if(f.forma.is(FormaGeometricaEnum.QUADRADO) && camadas > 3) {
            for (int camada = 4; camada <= camadas; camada++ ){
                if (camada % 2 != 0)
                    desenho = " stroke=\"black\" stroke-width=\"0\" fill=\"black\"/>";
                else
                    desenho = " stroke=\"blue\" stroke-width=\"2\" fill=\"white\"/>";
                String[] listPosicao = GeraInteriorQuadrado(f.tamanho, camada);
                if (camada % 2 != 0) {
                    for (String posicao: listPosicao) {
                        //gravarSaida.printf(geometrico + posicao + tamanho + desenho);
                    }
                }
            }
        }
        gravarSaida.printf("\n</svg>");
        gravarSaida.close();
        saida.close();
        System.out.println("Arquivo Gerado.");
        System.out.println(System.getProperties().getProperty("user.dir") + "\\saida_fractal.svg");
    }

    private static String GeraCentro(int posicao, int tamanho, FormaGeometricaEnum forma, boolean coordenada){
        if(forma.is(FormaGeometricaEnum.CIRCULO))
            return coordenada ? " cx=\"" + (posicao + tamanho) + "\"" : " cy=\"" + (posicao + tamanho) + "\"";
        else
            return coordenada ? " x=\"" + (posicao + tamanho) + "\"" : " y=\"" + (posicao + tamanho) + "\"";
    }

    private static String GeraPontasQuadrado(int posicao, int tamanho, boolean coordenada){
            return coordenada ? " x=\"" + (posicao + tamanho * 2) + "\"" : " y=\"" + (posicao + tamanho * 2) + "\"";
    }

    private static String[] GeraInteriorQuadrado(int tamanho, int camada) {
        int total = 0;
        String[] listPosicao;
        if (camada % 2 != 0) { // quadrados pretos
            listPosicao = new String[camada / 2];
            total = tamanho + camada * tamanho + 1;
        }
        else { // quadrados brancos
            listPosicao = new String[camada / 2];
            total = tamanho + (camada * tamanho) * 10 + 1;
        }
        for (int i = 1; i < camada / 2; i++)
            listPosicao[i] = " x=\"" + (total - tamanho) + "\" y=\"" + (camada * (tamanho *i)+ 1) + "\"";

        return listPosicao;


    }

    private static String GeraPontasCirculo(int posicao, int tamanho, int camada, int interacao, boolean coordenada) {
        double curvatura = (1 + ((camada * camada) * (interacao * 0.01)));
        double multiplicador = 1.5;
        return coordenada ? " cx=\"" + (posicao + tamanho * multiplicador) + "\"" + " cy=\"" + posicao * curvatura + "\"" :
                " cx=\"" + posicao * curvatura + "\"" + " cy=\"" + (posicao + tamanho * multiplicador) + "\"";
    }
}