export default function Button({ children, href, className }) {
  const base = "px-8 py-2 text-xl font-semibold rounded-sm max-w-fit";

  return (
    <a href={href} className={`${base} ${className}`}>
      {children}
    </a>
  );
}