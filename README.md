# SmartParts
PC-ről, RF-fel elért modulok szabályzása, user scriptek alapján. Relé modulok, fénymérők, kapcsolók, stb...


Egy java server fut egy PC-n, amit a kliens UDP broadcasting segítségével megtalál, és kapcsolódik hozzá.

A kliensben írhatunk scripteket, melyeket a server futtat eseményvezérelten, időzítve, vagy felhasználói indításra.
Ezek a scriptek a szerveren egy VM-en futnak, ami segítségével elérnek egy, a PC-re USB-vel csatlakoztatott eszközt,
mely RF segítségével további modulokkal kommunikál.

A modulok lehetnek relék, kapcsolók, ajtónyítás figyelők, fény/hang érzékelők, stb...

Ezek a modulok nem csak szabályozhatók, de bemenetként is funkcionálnak, így képesek eseményeket kiváltani a szerveren,
illetve állapotuk lekérdezhető. Mindez megvalósítható user scriptek segítségével.


Windowson tesztelve, kb. fut linuxon is.
