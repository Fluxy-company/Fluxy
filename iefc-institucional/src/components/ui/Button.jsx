export default function Button({ children, href, className }) {
  const base = "px-9 py-2 text-2xl font-bold rounded-md max-w-fit";

  return (
    <a href={href} className={`${base} ${className}`}>
      {children}
    </a>
  );
}