package priv.lee.cad.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

public abstract class StringUtils {

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    private static final String FOLDER_SEPARATOR = "/";

    public static final String EMPTY = "";

    public static final String [] EMPTY_STRING_ARRAY = new String [0];

    private static final String TOP_PATH = "..";

    public static final int INDEX_NOT_FOUND = -1;

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    public static String [] addStringToArray(String [] array, String str) {
	if (ObjectUtils.isEmpty (array)) {
	    return new String [] { str };
	}

	String [] newArr = new String [array.length + 1];
	System.arraycopy (array,0,newArr,0,array.length);
	newArr[array.length] = str;
	return newArr;
    }

    public static String applyRelativePath(String path, String relativePath) {
	int separatorIndex = path.lastIndexOf (FOLDER_SEPARATOR);
	if (separatorIndex != -1) {
	    String newPath = path.substring (0,separatorIndex);
	    if (!relativePath.startsWith (FOLDER_SEPARATOR)) {
		newPath += FOLDER_SEPARATOR;
	    }
	    return newPath + relativePath;
	} else {
	    return relativePath;
	}
    }

    public static String arrayToCommaDelimitedString(Object [] arr) {
	return arrayToDelimitedString (arr,",");
    }

    public static String arrayToDelimitedString(Object [] arr, String delim) {
	if (ObjectUtils.isEmpty (arr)) {
	    return "";
	}
	if (arr.length == 1) {
	    return ObjectUtils.nullSafeToString (arr[0]);
	}

	StringBuilder sb = new StringBuilder ();
	for (int i = 0; i < arr.length; i++) {
	    if (i > 0) {
		sb.append (delim);
	    }
	    sb.append (arr[i]);
	}
	return sb.toString ();
    }

    public static String capitalize(String str) {
	return changeFirstCharacterCase (str,true);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
	if (!hasLength (str)) {
	    return str;
	}

	char baseChar = str.charAt (0);
	char updatedChar;
	if (capitalize) {
	    updatedChar = Character.toUpperCase (baseChar);
	} else {
	    updatedChar = Character.toLowerCase (baseChar);
	}
	if (baseChar == updatedChar) {
	    return str;
	}

	char [] chars = str.toCharArray ();
	chars[0] = updatedChar;
	return new String (chars,0,chars.length);
    }

    public static String cleanPath(String path) {
	if (!hasLength (path)) {
	    return path;
	}
	String pathToUse = replace (path,WINDOWS_FOLDER_SEPARATOR,FOLDER_SEPARATOR);

	int prefixIndex = pathToUse.indexOf (':');
	String prefix = "";
	if (prefixIndex != -1) {
	    prefix = pathToUse.substring (0,prefixIndex + 1);
	    if (prefix.contains (FOLDER_SEPARATOR)) {
		prefix = "";
	    } else {
		pathToUse = pathToUse.substring (prefixIndex + 1);
	    }
	}
	if (pathToUse.startsWith (FOLDER_SEPARATOR)) {
	    prefix = prefix + FOLDER_SEPARATOR;
	    pathToUse = pathToUse.substring (1);
	}

	String [] pathArray = delimitedListToStringArray (pathToUse,FOLDER_SEPARATOR);
	LinkedList<String> pathElements = new LinkedList<> ();
	int tops = 0;

	for (int i = pathArray.length - 1; i >= 0; i--) {
	    String element = pathArray[i];
	    if (CURRENT_PATH.equals (element)) {
		// Points to current directory - drop it.
	    } else if (TOP_PATH.equals (element)) {
		// Registering top path found.
		tops++;
	    } else {
		if (tops > 0) {
		    // Merging path element with element corresponding to top
		    // path.
		    tops--;
		} else {
		    // Normal path element found.
		    pathElements.add (0,element);
		}
	    }
	}

	// Remaining top paths need to be retained.
	for (int i = 0; i < tops; i++) {
	    pathElements.add (0,TOP_PATH);
	}
	// If nothing else left, at least explicitly point to current path.
	if (pathElements.size () == 1 && "".equals (pathElements.getLast ()) && !prefix.endsWith (FOLDER_SEPARATOR)) {
	    pathElements.add (0,CURRENT_PATH);
	}

	return prefix + collectionToDelimitedString (pathElements,FOLDER_SEPARATOR);
    }

    public static String collectionToCommaDelimitedString(Collection<?> coll) {
	return collectionToDelimitedString (coll,",");
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
	return collectionToDelimitedString (coll,delim,"","");
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {

	if (CollectionUtils.isEmpty (coll)) {
	    return "";
	}

	StringBuilder sb = new StringBuilder ();
	Iterator<?> it = coll.iterator ();
	while (it.hasNext ()) {
	    sb.append (prefix).append (it.next ()).append (suffix);
	    if (it.hasNext ()) {
		sb.append (delim);
	    }
	}
	return sb.toString ();
    }

    public static Set<String> commaDelimitedListToSet(String str) {
	Set<String> set = new LinkedHashSet<> ();
	String [] tokens = commaDelimitedListToStringArray (str);
	for (String token : tokens) {
	    set.add (token);
	}
	return set;
    }

    public static String [] commaDelimitedListToStringArray(String str) {
	return delimitedListToStringArray (str,",");
    }

    public static String [] concatenateStringArrays(String [] array1, String [] array2) {
	if (ObjectUtils.isEmpty (array1)) {
	    return array2;
	}
	if (ObjectUtils.isEmpty (array2)) {
	    return array1;
	}

	String [] newArr = new String [array1.length + array2.length];
	System.arraycopy (array1,0,newArr,0,array1.length);
	System.arraycopy (array2,0,newArr,array1.length,array2.length);
	return newArr;
    }

    private static boolean containsText(CharSequence str) {
	int strLen = str.length ();
	for (int i = 0; i < strLen; i++) {
	    if (!Character.isWhitespace (str.charAt (i))) {
		return true;
	    }
	}
	return false;
    }

    public static boolean containsWhitespace(CharSequence str) {
	if (!hasLength (str)) {
	    return false;
	}

	int strLen = str.length ();
	for (int i = 0; i < strLen; i++) {
	    if (Character.isWhitespace (str.charAt (i))) {
		return true;
	    }
	}
	return false;
    }

    public static boolean containsWhitespace(String str) {
	return containsWhitespace ((CharSequence) str);
    }

    public static int countOccurrencesOf(String str, String sub) {
	if (!hasLength (str) || !hasLength (sub)) {
	    return 0;
	}

	int count = 0;
	int pos = 0;
	int idx;
	while (( idx = str.indexOf (sub,pos) ) != -1) {
	    ++count;
	    pos = idx + sub.length ();
	}
	return count;
    }

    public static String delete(String inString, String pattern) {
	return replace (inString,pattern,"");
    }

    public static String deleteAny(String inString, String charsToDelete) {
	if (!hasLength (inString) || !hasLength (charsToDelete)) {
	    return inString;
	}

	StringBuilder sb = new StringBuilder (inString.length ());
	for (int i = 0; i < inString.length (); i++) {
	    char c = inString.charAt (i);
	    if (charsToDelete.indexOf (c) == -1) {
		sb.append (c);
	    }
	}
	return sb.toString ();
    }

    public static String [] delimitedListToStringArray(String str, String delimiter) {
	return delimitedListToStringArray (str,delimiter,null);
    }

    public static String [] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {

	if (str == null) {
	    return new String [0];
	}
	if (delimiter == null) {
	    return new String [] { str };
	}

	List<String> result = new ArrayList<> ();
	if ("".equals (delimiter)) {
	    for (int i = 0; i < str.length (); i++) {
		result.add (deleteAny (str.substring (i,i + 1),charsToDelete));
	    }
	} else {
	    int pos = 0;
	    int delPos;
	    while (( delPos = str.indexOf (delimiter,pos) ) != -1) {
		result.add (deleteAny (str.substring (pos,delPos),charsToDelete));
		pos = delPos + delimiter.length ();
	    }
	    if (str.length () > 0 && pos <= str.length ()) {
		// Add rest of String, but not in case of empty input.
		result.add (deleteAny (str.substring (pos),charsToDelete));
	    }
	}
	return toStringArray (result);
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
	return ( str != null && suffix != null && str.length () >= suffix.length ()
		&& str.regionMatches (true,str.length () - suffix.length (),suffix,0,suffix.length ()) );
    }

    public static String getFilename(String path) {
	if (path == null) {
	    return null;
	}

	int separatorIndex = path.lastIndexOf (FOLDER_SEPARATOR);
	return ( separatorIndex != -1 ? path.substring (separatorIndex + 1) : path );
    }

    public static String getFilenameExtension(String path) {
	if (path == null) {
	    return null;
	}

	int extIndex = path.lastIndexOf (EXTENSION_SEPARATOR);
	if (extIndex == -1) {
	    return null;
	}

	int folderIndex = path.lastIndexOf (FOLDER_SEPARATOR);
	if (folderIndex > extIndex) {
	    return null;
	}

	return path.substring (extIndex + 1);
    }

    public static boolean hasLength(CharSequence str) {
	return ( str != null && str.length () > 0 );
    }

    public static boolean hasLength(String str) {
	return ( str != null && !str.isEmpty () );
    }

    public static boolean hasText(CharSequence str) {
	return ( str != null && str.length () > 0 && containsText (str) );
    }

    public static boolean hasText(String str) {
	return ( str != null && !str.isEmpty () && containsText (str) );
    }

    public static boolean isEmpty(Object str) {
	return ( str == null || "".equals (str) );
    }

    public static Locale parseLocale(String localeValue) {
	String [] tokens = tokenizeLocaleSource (localeValue);
	if (tokens.length == 1) {
	    return Locale.forLanguageTag (localeValue);
	}
	return parseLocaleTokens (localeValue,tokens);
    }

    public static Locale parseLocaleString(String localeString) {
	return parseLocaleTokens (localeString,tokenizeLocaleSource (localeString));
    }

    private static Locale parseLocaleTokens(String localeString, String [] tokens) {
	String language = ( tokens.length > 0 ? tokens[0] : "" );
	String country = ( tokens.length > 1 ? tokens[1] : "" );
	validateLocalePart (language);
	validateLocalePart (country);

	String variant = "";
	if (tokens.length > 2) {
	    // There is definitely a variant, and it is everything after the
	    // country
	    // code sans the separator between the country code and the variant.
	    int endIndexOfCountryCode = localeString.indexOf (country,language.length ()) + country.length ();
	    // Strip off any leading '_' and whitespace, what's left is the
	    // variant.
	    variant = trimLeadingWhitespace (localeString.substring (endIndexOfCountryCode));
	    if (variant.startsWith ("_")) {
		variant = trimLeadingCharacter (variant,'_');
	    }
	}
	return ( language.length () > 0 ? new Locale (language,country,variant) : null );
    }

    public static TimeZone parseTimeZoneString(String timeZoneString) {
	TimeZone timeZone = TimeZone.getTimeZone (timeZoneString);
	if ("GMT".equals (timeZone.getID ()) && !timeZoneString.startsWith ("GMT")) {
	    // We don't want that GMT fallback...
	    throw new IllegalArgumentException ("Invalid time zone specification '" + timeZoneString + "'");
	}
	return timeZone;
    }

    public static boolean pathEquals(String path1, String path2) {
	return cleanPath (path1).equals (cleanPath (path2));
    }

    public static String quote(String str) {
	return ( str != null ? "'" + str + "'" : null );
    }

    public static Object quoteIfString(Object obj) {
	return ( obj instanceof String ? quote ((String) obj) : obj );
    }

    public static String [] removeDuplicateStrings(String [] array) {
	if (ObjectUtils.isEmpty (array)) {
	    return array;
	}

	Set<String> set = new LinkedHashSet<> ();
	for (String element : array) {
	    set.add (element);
	}
	return toStringArray (set);
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
	if (!hasLength (inString) || !hasLength (oldPattern) || newPattern == null) {
	    return inString;
	}
	int index = inString.indexOf (oldPattern);
	if (index == -1) {
	    // no occurrence -> can return input as-is
	    return inString;
	}

	int capacity = inString.length ();
	if (newPattern.length () > oldPattern.length ()) {
	    capacity += 16;
	}
	StringBuilder sb = new StringBuilder (capacity);

	int pos = 0; // our position in the old string
	int patLen = oldPattern.length ();
	while (index >= 0) {
	    sb.append (inString.substring (pos,index));
	    sb.append (newPattern);
	    pos = index + patLen;
	    index = inString.indexOf (oldPattern,pos);
	}

	// append any characters to the right of a match
	sb.append (inString.substring (pos));
	return sb.toString ();
    }

    public static String [] sortStringArray(String [] array) {
	if (ObjectUtils.isEmpty (array)) {
	    return new String [0];
	}

	Arrays.sort (array);
	return array;
    }

    public static String [] split(String toSplit, String delimiter) {
	if (!hasLength (toSplit) || !hasLength (delimiter)) {
	    return null;
	}
	int offset = toSplit.indexOf (delimiter);
	if (offset < 0) {
	    return null;
	}

	String beforeDelimiter = toSplit.substring (0,offset);
	String afterDelimiter = toSplit.substring (offset + delimiter.length ());
	return new String [] { beforeDelimiter, afterDelimiter };
    }

    public static Properties splitArrayElementsIntoProperties(String [] array, String delimiter) {
	return splitArrayElementsIntoProperties (array,delimiter,null);
    }

    public static Properties splitArrayElementsIntoProperties(String [] array, String delimiter, String charsToDelete) {

	if (ObjectUtils.isEmpty (array)) {
	    return null;
	}

	Properties result = new Properties ();
	for (String element : array) {
	    if (charsToDelete != null) {
		element = deleteAny (element,charsToDelete);
	    }
	    String [] splittedElement = split (element,delimiter);
	    if (splittedElement == null) {
		continue;
	    }
	    result.setProperty (splittedElement[0].trim (),splittedElement[1].trim ());
	}
	return result;
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
	return ( str != null && prefix != null && str.length () >= prefix.length ()
		&& str.regionMatches (true,0,prefix,0,prefix.length ()) );
    }

    public static String stripFilenameExtension(String path) {
	int extIndex = path.lastIndexOf (EXTENSION_SEPARATOR);
	if (extIndex == -1) {
	    return path;
	}

	int folderIndex = path.lastIndexOf (FOLDER_SEPARATOR);
	if (folderIndex > extIndex) {
	    return path;
	}

	return path.substring (0,extIndex);
    }

    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
	if (index + substring.length () > str.length ()) {
	    return false;
	}
	for (int i = 0; i < substring.length (); i++) {
	    if (str.charAt (index + i) != substring.charAt (i)) {
		return false;
	    }
	}
	return true;
    }

    private static String [] tokenizeLocaleSource(String localeSource) {
	return tokenizeToStringArray (localeSource,"_ ",false,false);
    }

    public static String [] tokenizeToStringArray(String str, String delimiters) {
	return tokenizeToStringArray (str,delimiters,true,true);
    }

    public static String [] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
	    boolean ignoreEmptyTokens) {

	if (str == null) {
	    return new String [0];
	}

	StringTokenizer st = new StringTokenizer (str,delimiters);
	List<String> tokens = new ArrayList<> ();
	while (st.hasMoreTokens ()) {
	    String token = st.nextToken ();
	    if (trimTokens) {
		token = token.trim ();
	    }
	    if (!ignoreEmptyTokens || token.length () > 0) {
		tokens.add (token);
	    }
	}
	return toStringArray (tokens);
    }

    public static String [] toStringArray(Collection<String> collection) {
	return collection.toArray (new String [0]);
    }

    public static String [] toStringArray(Enumeration<String> enumeration) {
	return toStringArray (Collections.list (enumeration));
    }

    public static String trimAllWhitespace(String str) {
	if (!hasLength (str)) {
	    return str;
	}

	int len = str.length ();
	StringBuilder sb = new StringBuilder (str.length ());
	for (int i = 0; i < len; i++) {
	    char c = str.charAt (i);
	    if (!Character.isWhitespace (c)) {
		sb.append (c);
	    }
	}
	return sb.toString ();
    }

    public static String [] trimArrayElements(String [] array) {
	if (ObjectUtils.isEmpty (array)) {
	    return new String [0];
	}

	String [] result = new String [array.length];
	for (int i = 0; i < array.length; i++) {
	    String element = array[i];
	    result[i] = ( element != null ? element.trim () : null );
	}
	return result;
    }

    public static String trimLeadingCharacter(String str, char leadingCharacter) {
	if (!hasLength (str)) {
	    return str;
	}

	StringBuilder sb = new StringBuilder (str);
	while (sb.length () > 0 && sb.charAt (0) == leadingCharacter) {
	    sb.deleteCharAt (0);
	}
	return sb.toString ();
    }

    public static String trimLeadingWhitespace(String str) {
	if (!hasLength (str)) {
	    return str;
	}

	StringBuilder sb = new StringBuilder (str);
	while (sb.length () > 0 && Character.isWhitespace (sb.charAt (0))) {
	    sb.deleteCharAt (0);
	}
	return sb.toString ();
    }

    public static String trimTrailingCharacter(String str, char trailingCharacter) {
	if (!hasLength (str)) {
	    return str;
	}

	StringBuilder sb = new StringBuilder (str);
	while (sb.length () > 0 && sb.charAt (sb.length () - 1) == trailingCharacter) {
	    sb.deleteCharAt (sb.length () - 1);
	}
	return sb.toString ();
    }

    public static String trimTrailingWhitespace(String str) {
	if (!hasLength (str)) {
	    return str;
	}

	StringBuilder sb = new StringBuilder (str);
	while (sb.length () > 0 && Character.isWhitespace (sb.charAt (sb.length () - 1))) {
	    sb.deleteCharAt (sb.length () - 1);
	}
	return sb.toString ();
    }

    public static String trimWhitespace(String str) {
	if (!hasLength (str)) {
	    return str;
	}

	int beginIndex = 0;
	int endIndex = str.length () - 1;

	while (beginIndex <= endIndex && Character.isWhitespace (str.charAt (beginIndex))) {
	    beginIndex++;
	}

	while (endIndex > beginIndex && Character.isWhitespace (str.charAt (endIndex))) {
	    endIndex--;
	}

	return str.substring (beginIndex,endIndex + 1);
    }

    public static String uncapitalize(String str) {
	return changeFirstCharacterCase (str,false);
    }

    public static String unqualify(String qualifiedName) {
	return unqualify (qualifiedName,'.');
    }

    public static String unqualify(String qualifiedName, char separator) {
	return qualifiedName.substring (qualifiedName.lastIndexOf (separator) + 1);
    }

    public static String uriDecode(String source, Charset charset) {
	int length = source.length ();
	if (length == 0) {
	    return source;
	}
	ClientAssert.notNull (charset,"Charset must not be null");

	ByteArrayOutputStream bos = new ByteArrayOutputStream (length);
	boolean changed = false;
	for (int i = 0; i < length; i++) {
	    int ch = source.charAt (i);
	    if (ch == '%') {
		if (i + 2 < length) {
		    char hex1 = source.charAt (i + 1);
		    char hex2 = source.charAt (i + 2);
		    int u = Character.digit (hex1,16);
		    int l = Character.digit (hex2,16);
		    if (u == -1 || l == -1) {
			throw new IllegalArgumentException (
				"Invalid encoded sequence \"" + source.substring (i) + "\"");
		    }
		    bos.write ((char) ( ( u << 4 ) + l ));
		    i += 2;
		    changed = true;
		} else {
		    throw new IllegalArgumentException ("Invalid encoded sequence \"" + source.substring (i) + "\"");
		}
	    } else {
		bos.write (ch);
	    }
	}
	return ( changed ? new String (bos.toByteArray (),charset) : source );
    }

    private static void validateLocalePart(String localePart) {
	for (int i = 0; i < localePart.length (); i++) {
	    char ch = localePart.charAt (i);
	    if (ch != ' ' && ch != '_' && ch != '#' && !Character.isLetterOrDigit (ch)) {
		throw new IllegalArgumentException ("Locale part \"" + localePart + "\" contains invalid characters");
	    }
	}
    }

    public static boolean equals(String string1, String string2) {
	if (string1 == null) {
	    return ( string2 == null );
	} else {
	    return string1.equals (string2);
	}
    }

    public static boolean equalsIgnoreCase(final CharSequence str1, final CharSequence str2) {
	if (str1 == null || str2 == null) {
	    return str1 == str2;
	} else if (str1 == str2) {
	    return true;
	} else if (str1.length () != str2.length ()) {
	    return false;
	} else {
	    return regionMatches (str1,true,0,str2,0,str1.length ());
	}
    }

    public static String substring(final String str, int start) {
	if (str == null) {
	    return null;
	}

	// handle negatives, which means last n characters
	if (start < 0) {
	    start = str.length () + start; // remember start is negative
	}

	if (start < 0) {
	    start = 0;
	}
	if (start > str.length ()) {
	    return EMPTY;
	}

	return str.substring (start);
    }

    public static String substring(final String str, int start, int end) {
	if (str == null) {
	    return null;
	}

	// handle negatives
	if (end < 0) {
	    end = str.length () + end; // remember end is negative
	}
	if (start < 0) {
	    start = str.length () + start; // remember start is negative
	}

	// check length next
	if (end > str.length ()) {
	    end = str.length ();
	}

	// if start is greater than end, return ""
	if (start > end) {
	    return EMPTY;
	}

	if (start < 0) {
	    start = 0;
	}
	if (end < 0) {
	    end = 0;
	}

	return str.substring (start,end);
    }

    public static String substringBefore(final String str, final String separator) {
	if (isEmpty (str) || separator == null) {
	    return str;
	}
	if (separator.isEmpty ()) {
	    return EMPTY;
	}
	final int pos = str.indexOf (separator);
	if (pos == INDEX_NOT_FOUND) {
	    return str;
	}
	return str.substring (0,pos);
    }

    public static String substringAfter(final String str, final String separator) {
	if (isEmpty (str)) {
	    return str;
	}
	if (separator == null) {
	    return EMPTY;
	}
	final int pos = str.indexOf (separator);
	if (pos == INDEX_NOT_FOUND) {
	    return EMPTY;
	}
	return str.substring (pos + separator.length ());
    }

    public static String substringBeforeLast(final String str, final String separator) {
	if (isEmpty (str) || isEmpty (separator)) {
	    return str;
	}
	final int pos = str.lastIndexOf (separator);
	if (pos == INDEX_NOT_FOUND) {
	    return str;
	}
	return str.substring (0,pos);
    }

    public static String substringAfterLast(final String str, final String separator) {
	if (isEmpty (str)) {
	    return str;
	}
	if (isEmpty (separator)) {
	    return EMPTY;
	}
	final int pos = str.lastIndexOf (separator);
	if (pos == INDEX_NOT_FOUND || pos == str.length () - separator.length ()) {
	    return EMPTY;
	}
	return str.substring (pos + separator.length ());
    }

    public static String substringBetween(final String str, final String tag) {
	return substringBetween (str,tag,tag);
    }

    public static String substringBetween(final String str, final String open, final String close) {
	if (str == null || open == null || close == null) {
	    return null;
	}
	final int start = str.indexOf (open);
	if (start != INDEX_NOT_FOUND) {
	    final int end = str.indexOf (close,start + open.length ());
	    if (end != INDEX_NOT_FOUND) {
		return str.substring (start + open.length (),end);
	    }
	}
	return null;
    }

    public static String [] substringsBetween(final String str, final String open, final String close) {
	if (str == null || isEmpty (open) || isEmpty (close)) {
	    return null;
	}
	final int strLen = str.length ();
	if (strLen == 0) {
	    return EMPTY_STRING_ARRAY;
	}
	final int closeLen = close.length ();
	final int openLen = open.length ();
	final List<String> list = new ArrayList<> ();
	int pos = 0;
	while (pos < strLen - closeLen) {
	    int start = str.indexOf (open,pos);
	    if (start < 0) {
		break;
	    }
	    start += openLen;
	    final int end = str.indexOf (close,start);
	    if (end < 0) {
		break;
	    }
	    list.add (str.substring (start,end));
	    pos = end + closeLen;
	}
	if (list.isEmpty ()) {
	    return null;
	}
	return list.toArray (new String [list.size ()]);
    }

    public static boolean contains(final CharSequence seq, final int searchChar) {
	if (isEmpty (seq)) {
	    return false;
	}
	return indexOf (seq,searchChar,0) >= 0;
    }

    public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
	if (seq == null || searchSeq == null) {
	    return false;
	}
	return indexOf (seq,searchSeq,0) >= 0;
    }

    public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
	if (str == null || searchStr == null) {
	    return false;
	}
	final int len = searchStr.length ();
	final int max = str.length () - len;
	for (int i = 0; i <= max; i++) {
	    if (regionMatches (str,true,i,searchStr,0,len)) {
		return true;
	    }
	}
	return false;
    }

    private static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
	    final CharSequence substring, final int start, final int length) {
	if (cs instanceof String && substring instanceof String) {
	    return ( (String) cs ).regionMatches (ignoreCase,thisStart,(String) substring,start,length);
	}
	int index1 = thisStart;
	int index2 = start;
	int tmpLen = length;

	// Extract these first so we detect NPEs the same as the
	// java.lang.String version
	final int srcLen = cs.length () - thisStart;
	final int otherLen = substring.length () - start;

	// Check for invalid parameters
	if (thisStart < 0 || start < 0 || length < 0) {
	    return false;
	}

	// Check that the regions are long enough
	if (srcLen < length || otherLen < length) {
	    return false;
	}

	while (tmpLen-- > 0) {
	    final char c1 = cs.charAt (index1++);
	    final char c2 = substring.charAt (index2++);

	    if (c1 == c2) {
		continue;
	    }

	    if (!ignoreCase) {
		return false;
	    }

	    // The same check as in String.regionMatches():
	    if (Character.toUpperCase (c1) != Character.toUpperCase (c2)
		    && Character.toLowerCase (c1) != Character.toLowerCase (c2)) {
		return false;
	    }
	}

	return true;
    }

    private static int indexOf(CharSequence cs, int searchChar, int start) {
	if (cs instanceof String) {
	    return ( (String) cs ).indexOf (searchChar,start);
	}
	final int sz = cs.length ();
	if (start < 0) {
	    start = 0;
	}
	if (searchChar < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
	    for (int i = start; i < sz; i++) {
		if (cs.charAt (i) == searchChar) {
		    return i;
		}
	    }
	}
	// supplementary characters (LANG1300)
	if (searchChar <= Character.MAX_CODE_POINT) {
	    final char [] chars = Character.toChars (searchChar);
	    for (int i = start; i < sz - 1; i++) {
		final char high = cs.charAt (i);
		final char low = cs.charAt (i + 1);
		if (high == chars[0] && low == chars[1]) {
		    return i;
		}
	    }
	}
	return INDEX_NOT_FOUND;
    }

    private static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
	return cs.toString ().indexOf (searchChar.toString (),start);
    }
}