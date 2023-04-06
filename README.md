# Mandelbrot
Mikołaj Hudko

## Co to?
Program do robienia zdjęcć oraz animacji wizualizacji zbioru Mandelbrota.

## Obsługa
Na środku widoczne jest obecnie zrobione zdjęcie. Można sterować nim za pomocą następujących przycisków:
 * `WASD` do poruszania się,
 * `R` do zbliżenia, `F` do oddalenia,
 * `Q` do ponownego zrobienia zdjęcia,
 * `X` do zwiększenia dokładności obliczania, `Z` do zmniejszenia,
 * `Y` do zmieniania między wyświetlaniem zbioru Julii a Mandelbrota

### Zakładka `Image`
Po lewej części znajdują się ustawienia rozdzielczości obrazu. Po prawej są przyciski do ponownego zrobienia zdjęcia oraz zapisania obecnego.

### Zakładka `Render settings`
Tutaj znajdują się ustawienia zdjęcia. Po kolei:
 * Sposób tworzenia zdjęcia:
   * Zbiór Mandelbrota z dokładnością float
   * Zbiór Mandelbrota z dokładnością double
   * Zbiór Julii z dokładnością double
   * Wyświetlanie obecnej palety kolorów
 * Wyświetlanie celownika na środku ekranu w celach demonstracyjnych
 * Opcje supersamplingu, czyli robienia zdjęcia w wyższej rozdzielczości, a następnie zmniejszania go w celu zwiększenia jakości.
 * Algorytm kolorowania (opcja `Smooth` nie działa dobrze ze zbiorami Julii) oraz paleta kolorów

### Zakładka `Position`
Tutaj znajdują się wartości części rzeczywistej i urojonej środka zdjęcia oraz przybliżenie i dokładność obliczania `threshold`.

### Zakładka `Animation`
Tutaj znajduje się edytor animacji. Na górze są na linii czasowej punkty, między którymi będzie poruszała się kamera. Można je przesuwać, oraz edytować po kliknięciu na nich `ppm`. Opcją `Set` można zmienić opcje obrazu tego punktu na obecne. Opcją `Go` można ustawić obecne opcje obrazu na te z punktu.
Na dole znajdują się przyciski do kontrolowania linią czasową, liczba klatek na sekundę oraz przycisk do nagrania filmu.