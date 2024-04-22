/*---   1ra Area: Codigo de Usuario   ---*/
//---> Paquetes e importaciones
package org.zafkiel;
import java_cup.runtime.*;
import java.util.LinkedList;
/*---   2da Area: Opciones y Declaraciones   ---*/

%%
%{
    //---> Codigo de usuario en sintaxis java
    public static LinkedList<TError> TablaEL = new LinkedList<TError>();
    public static String getNombreToken(int sym) {
            switch(sym) {
                case Simbolos.mas: return "mas";
                case Simbolos.menos: return "menos";
                case Simbolos.por: return "por";
                case Simbolos.div: return "div";
                case Simbolos.para: return "para";
                case Simbolos.parc: return "parc";
                case Simbolos.num: return "num";
                case Simbolos.error: return "error";
                default: return "desconocido";
            }
        }
%}

//---> Directivas
%public
%class Analizador_Lexico
%cupsym Simbolos
%cup
%char
%column
%full
%line


//---Expresiones Regulares
numero = [0-9]+

%%
/*---   3ra Area: Reglas Lexicas   ---*/
"accion"    { return new Symbol(Simbolos.ACCION, yycolumn, yyline, yytext()); }
//---> Simbolos
<YYINITIAL> "+"         { return new Symbol(Simbolos.mas, yycolumn, yyline, yytext()); }
<YYINITIAL> "-"         { return new Symbol(Simbolos.menos, yycolumn, yyline, yytext()); }
<YYINITIAL> "*"         { return new Symbol(Simbolos.por, yycolumn, yyline, yytext()); }
<YYINITIAL> "/"         { return new Symbol(Simbolos.div, yycolumn, yyline, yytext()); }
<YYINITIAL> "("         { return new Symbol(Simbolos.para, yycolumn, yyline, yytext()); }
<YYINITIAL> ")"         { return new Symbol(Simbolos.parc, yycolumn, yyline, yytext()); }

//---> Simbolos ER
<YYINITIAL> {numero}    { return new Symbol(Simbolos.num, yycolumn, yyline, yytext()); }

//--->Espacios
[ \t\r\n\f]             {/* Espacios en blanco, se ignoran */}

//---> Errores Lexicos
.                       { return new Symbol(Simbolos.error, yycolumn, yyline, yytext()); }