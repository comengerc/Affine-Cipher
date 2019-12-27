

//							OSMAN MANTICI
//								150117505
//					 CSE 2023 HOMEWORK #4


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AffineCipher {
	
	public static int number_a=0; //encrypt için global a
	public static int number_b=0; //encrypt için global b
	public static int numOfInDict=0; //sözlükteki kelime sayısı
	public static int decrypt_number_a=0; //decrypt için global a
	public static int decrypt_number_b=0; //decrypt için global b
	
	public static ArrayList<String> wordDraft = new ArrayList<String>(); //kullanmamışız unutmuşuz...
	public static ArrayList<String> solution = new ArrayList<String>(); //sonuç arraylisti
	public static ArrayList<String> dictionary = new ArrayList<String>(); //sözlükteki kelimeleri tutar.
	public static ArrayList<String> moreThanTree = new ArrayList<String>(); //uzunluğu 3tne fazla olan kelimeleri tutar.
	public static ArrayList<String> decOut = new ArrayList<String>(); //decrypt edilecek kelimeleri tutar
	
	static int modInverse(int a){  //decryption için a'nın mod inverse'ünü alır.
        a = a % 26; //a'nın 26'ya göre modu alınır.
        for (int x = 1; x < 26; x++) 
           if ((a * x) % 26 == 1) //a'nın x ile çarpımının modu 1 ise mode inverse x demektir. sonuç olarak x döner
              return x; 
        return 1; 
    } 	
	
	public static int gcd(int p, int q) { //gcd bulmak için yazıldı.
	    if (q == 0) return p;
	    if (p == 0) return q;

	    if ((p & 1) == 0 && (q & 1) == 0) return gcd(p >> 1, q >> 1) << 1;

	    else if ((p & 1) == 0) return gcd(p >> 1, q);

	    else if ((q & 1) == 0) return gcd(p, q >> 1);

	    else if (p >= q) return gcd((p-q) >> 1, q);

	    else return gcd(p, (q-p) >> 1);
	}
	
	public static char encrypt(char token, int num_a, int num_b){ //a ve b değerlerine göre karakteri encrypt etmek için yazılmıştır.
																// parametre olarak bir karakter, int a ve int b değerleri alır.

		token  = Character.toUpperCase(token); //karakteri uppercase yapar.
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //alfabe için yazdığımız string.
		char[] alphabet = str.toCharArray(); //alfabe arrayimiz
		int func;
		
		for(int i=0; i<str.length(); i++) { 
			if(token==alphabet[i]) {
				func = (i*num_a) + num_b; //bu formüle göre encrypt edilir. doğru denklemi bu. y=ax+b;
				func = func%26; //bunun 26ya göre mode'u alınır.
				token = alphabet[func]; //karakteri bulduğumuz sayıya göre alfabeden bi karakterle eşler ve eşlenen karakteri döndürür. 
				return token;
			}
		}

		return token;
		
	}
	
	public static boolean decryptGuess(ArrayList<String> list) { //arraylisti parametre olarak alır.
		
//		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		char[] alphabet = str.toCharArray();
		int[] a = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25}; //26 ile aralarında asal olan sayılar
		int[] b = new int[26];
		
		
		String msg= "";
		
		for(int i=0; i<b.length; i++) { //1'den 26'ya kadar olan sayıları b arrayine atıyoruz.
			b[i]=i;
		}

		for(String token:list) { //listedeki her bir eleman için,
			for(int k=0; k<a.length; k++) { //a arrayindeki tüm a'ları dener.
				for(int j=0; j<26; j++) { //b arrayindeki tüm b'leri dener.
					for(int i=0; i<token.length(); i++) { //kelimeyi okur ve a ve b değerlerine göre decrypt etmeye çalışır.
						msg = msg + (char) (((modInverse(a[k]) * (token.charAt(i) + 'A' - j) % 26      ))    + 'A'); // +'A' ascii table için 
						//msg bizim decrypt sonucumuz 
					}
					if(isInDictionary(msg)) { //bu method bulunan mesaj sözlükte mi diye kontrol eder.
						numOfInDict++; //eğer sözlükteyse, sadece sözlükte bulunan kelime sayısını tuttuğumuz int değeri 1 arttırır.
					}
					if(numOfInDict>=2) { //sözlükte bulduğumuz kelime sayısı 3ü geçerse, doğru a ve b değerlerini bulduk demektir. fonksiyon true döndürür.
						decrypt_number_a=a[k]; //decryption için kullanacağımız a'yı assign ederiz. global değişkendir.
						decrypt_number_b=j; //decryption için kullanacağımız b'yi assign ederiz. global değişkendir.
						return true;
					}

					msg=""; //msg değerini yeni bir kelime için boşaltırız.
				}			
			}
		}
		
		return false;
	}

	public static void decryptAll(ArrayList<String> toBeDecrypt, int dec_a, int dec_b){ //parametre olarak bir arraylist,int a ve  int b değerleri alır. 
																						//a ve b ye göre decrypt et demektir
		String result=""; //sonuç kelimesini tutmak için oluşturdur.
		for(String word : toBeDecrypt) { //for each loopu. toBeDecrypt listesindeki herbir kelimeyi decrypt eder a ve b'ye göre.
			for(int i=0; i<word.length(); i++) { //harf harf decryption işlemi
				result = result + (char) (((modInverse(dec_a) * (word.charAt(i) + 'A' - dec_b) % 26      ))    + 'A');
				
			}
			solution.add(result); //decrypt işleminin sonucu solution arraylistine atılır. bu bizden istenen sonuçtur.  
			result=""; //sonuç kelimesini yenisi için boşaltır. 
		}

	}
	
	public static boolean isInDictionary(String word) { //kelime sözlükte bulunuyor mu diye bakar.
		
		for(int i=0; i<dictionary.size();i++) { //sözlük boyunca döner
			if(word.equals(dictionary.get(i))) { //kelime sözlükte varsa true döndürür
				return true;
			}
		}	
		return false; //yoksa false.
	}
	
	public static void printList(List<String> list) { //array listi bastırır. 
		for(String word:list) { //for each döngüsüdür. stringlerden oluşan array listin içindeki tüm stringler için döngü içindeki işlemi yapar. 
			System.out.print(word + " ");
		}
	}
	
	public static void main(String[] args) {
		
		File f = new File("input2.txt"); //dosyayı okuyoruz
		
		try {
			
			Random random = new Random(); 
			do {

				number_a = random.nextInt(26); //0 ile 26 arasında bi random seçiyoruz.
												//eğer 26 ile gcd'si 1den fazla ise, yani random a ve 26 aralarında asal değillerse yeniden a seçiyoruz.
												// ta ki 26 ile arasında asal bir a bulana kadar.
			}while(gcd(number_a,26)!=1);
			
			number_b=random.nextInt(26); // random bi b değeri oluşturuyoruz. 
			
			FileReader fr = new FileReader(f); //reader açıyoruz. 

			int c = fr.read();
			
			File output = new File("output2.txt"); //encrypt ettiğimiz metni yazacağımız dosyayı açıyoruz.
			FileWriter fw = new FileWriter(output); //writer açıyoruz

			do{ //input dosyasını char by char okuyoruz ve bunu az evvel random belirlediğimiz a ve b'ye göre şifreliyoruz. 
				char k = (char)c; 
				k = Character.toUpperCase(k); //tüm okunan harfleri büyütüyoruz alfabemizle uyumlu olması ve sadece 26 harf olması için
				c = fr.read();
				
				k = encrypt(k,number_a,number_b);	//harf harf şifreliyoruz			

				fw.write(k); //sonuç dosyasına şifrelenmiş karakteri yazıyoruz.
				
			}while(c!=-1);
			
			fr.close(); //readerı kapatıyoruz.
			fw.close(); //writerı kapatıyoruz.
		}
		catch(Exception e) {
			 e.printStackTrace();
		}
		
		//encryption işlemi burada tamamlanmış oluyor.
		
		
		File datFile = new File("HW4_dictionary.dat");  //decrypt etmeye çalıştığımız kelimeler karşılaştırmak için bir sözlük okuması işlemi yapıyoruz.
		
		try {
			FileReader datReader = new FileReader(datFile); //sözlük için reader açıyoruz
			@SuppressWarnings("resource")
			BufferedReader b = new BufferedReader(new FileReader(datFile)); 

            String readLine = "";

            while ((readLine = b.readLine()) != null) { //line line okuyoruz. fakat her lineda bir kelime var. yani okuduğumuz her kelimeyi bu şekilde alabiliriz.
            	dictionary.add(readLine.toUpperCase()); //dictionary adında bir arraylistimiz var. okunan her kelimeyi buna yazıyoruz ki her seferinde dözlük 
            											//dosyasını okumak zorunda kalmayalım.
            }
					
			datReader.close(); //sözlük okuyucusunu kapatıyoruz.
		}
		catch(Exception e){
			 e.printStackTrace();
		}
		
		File f2 = new File("output2.txt"); // decryption işleminin sonucu için bi output dosyası açıyoruz. bu bizim encrypt işlemimizin sonucu aslında
		
		String token = "";
//		String result = "";
		
		try {
			
			FileReader fr2 = new FileReader(f2); //decryption için reader açıyoruz
			int c2 = fr2.read();
			
			while(c2!=-1){
				char d = (char)c2; //okuma char by char gerçekleşiyor
				d = Character.toUpperCase(d);  //okunan karakterleri upper case yapıyoruz.
				c2 = fr2.read();
				
				if(!Character.isWhitespace(d)) {  // karakter boşluk değil ise stringe karakteri ekliyoruz
					
					token += d;
				}
				else  if(Character.isWhitespace(d)&&c2!=-1){ //karakter boşluksa ve okuma sonlanmadıysa 
					if(token.length()>=4) { //kelimenin uzunluğu 4ten fazlaysa morethanthree arraylistine ekliyoruz. 
											//bu arraylist, uzunluğu 4ten fazla olan kelimeleri tutuyor. decryption için sözlükte bulmayı deneyeceğimiz bulunması muhtemel kelime
											//listesidir bu.
											//ilk aşamada a ve byi bulana kadar bu listeyi decrypt ediyoruz.
						moreThanTree.add(token);
					}

					decOut.add(token); //karakter boşluksa, artık bir kelime elde ettik demektir. decout arraylistine ekliyoruz. a ve b değerlerini bulduğumuzda
										//bu listedeki kelimeleri decrypt edeceğiz.
					token = ""; //kelime aldığımız zaman kelimeyi tutan token stringimizi yeni kelime için boşaltıyoruz. 
					continue;
				}else if (Character.isWhitespace(d)&&c2==-1) {
					continue;
				}
							
			}
			

			fr2.close();

		}
		catch(Exception e) {
			 e.printStackTrace();
		}	
		
		if(decryptGuess(moreThanTree)) { // bu fonksiyon boolean döndürür. doğru ise, bir a ve b değeri bulmuşuz demektir.
			decryptAll(decOut, decrypt_number_a, decrypt_number_b); //decout arraylistindeki tüm kelimeler, decryptGuess fonksiyounda bulduğumuz a ve b değerlerine göre 
																	//decrypt edilir
		}
		
		printList(solution); //decryption sonucunu konsola bastırıyoruz. bu fonksiyor arraylistin tüm elemanlarını yazdırmak için yazılmıştır.
	}

}


