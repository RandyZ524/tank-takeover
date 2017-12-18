public enum HelpType {
	CIRCLE, SPIRAL, RANDOM, HOMING, AUTO;
	
	public static String[] names() {
		HelpType[] types = values();
		String[] names = new String[types.length];
		
		for (int i = 0; i < types.length; i++)
			names[i] = types[i].name();
		
		return names;
	}
	
}