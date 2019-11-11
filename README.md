
# Ackermann Function AsyncTask
Application calculates value of Ackermann function using AsyncTask. Common problem of AsyncTask such as view leaking, configuration change handling are solved via retain fragment, design principles (SOLID).

Ackermann function values is calculated recursively:<br/>
![Recursive definition](https://wikimedia.org/api/rest_v1/media/math/render/svg/1a15ea2fcf1977e497bccdf1916ae23edc412fff)

## Usage
<table>
	<tr>
		<th>Action</th><th>Screenshot</th>
	</tr>
	<tr>
		<td>Start application</td><td><img 
src="https://drive.google.com/uc?export=view&id=1o-R9OwnoR9rR1ctsJjOaOJGdmsSZcdIu" 
data-canonical-src="https://drive.google.com/uc?export=view&id=1o-R9OwnoR9rR1ctsJjOaOJGdmsSZcdIu" width="200" /></td>
	</tr>
	<tr>
		<td>Calculation process</td><td><img 
src="https://drive.google.com/uc?export=view&id=1axyQhvTIOXwrj5etedBkveFgduoErsxY" 
data-canonical-src="https://drive.google.com/uc?export=view&id=1axyQhvTIOXwrj5etedBkveFgduoErsxY" width="200" /></td>
	</tr>
	<tr>
		<td>Calculation result</td><td><img 
src="https://drive.google.com/uc?export=view&id=1RY3EAucSv28cFc_mLR8nzmtmIvaODvNk" 
data-canonical-src="https://drive.google.com/uc?export=view&id=1RY3EAucSv28cFc_mLR8nzmtmIvaODvNk" width="200" /></td>
	</tr>
	<tr>
		<td>Stack overflow error result</td><td><img 
src="https://drive.google.com/uc?export=view&id=1d6afBiz6sWzs42MsPiuft1SV3S8D0zrS" 
data-canonical-src="https://drive.google.com/uc?export=view&id=1d6afBiz6sWzs42MsPiuft1SV3S8D0zrS" width="200" /></td>
	</tr>
	<tr>
		<td>Wrong arguments error result</td><td><img 
src="https://drive.google.com/uc?export=view&id=1_ygcqlwSefnFEavDVFDbS2_arn6kByJ_" 
data-canonical-src="https://drive.google.com/uc?export=view&id=1_ygcqlwSefnFEavDVFDbS2_arn6kByJ_" width="200" /></td>
	</tr>
</table>

## References  
Android developers documentation:  
* AsyncTask - [https://developer.android.com/reference/kotlin/android/os/AsyncTask](https://developer.android.com/reference/kotlin/android/os/AsyncTask)
* Handling configuration changes with fragments - [https://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html](https://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html)

Java documentation:
* WeakReference - 
[https://docs.oracle.com/javase/7/docs/api/java/lang/ref/WeakReference.html](https://docs.oracle.com/javase/7/docs/api/java/lang/ref/WeakReference.html)

Ackermann function description:
* [https://en.wikipedia.org/wiki/Ackermann_function](https://en.wikipedia.org/wiki/Ackermann_function)
* [https://studbooks.net/2195922/matematika_himiya_fizika/funktsiya_akkermana](https://studbooks.net/2195922/matematika_himiya_fizika/funktsiya_akkermana)
